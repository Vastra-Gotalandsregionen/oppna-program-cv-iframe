package se.vgregion.portal.repository;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.commons.beanutils.BeanUtils;
import se.vgregion.portal.iframe.model.UserSiteCredential;
import se.vgregion.portal.iframe.util.CryptoUtil;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.FileWriter;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class JDBCCredentialStoreRepository extends SimpleJdbcDaoSupport implements CredentialStoreRepository {
    private static Logger log = LoggerFactory.getLogger(JDBCCredentialStoreRepository.class);

    private String dbCredentialFile;

    private CryptoUtil cryptoUtils;

    public void setCryptoUtils(CryptoUtil cryptoUtils) {
       this.cryptoUtils = cryptoUtils;
    }

    public void setDbCredentialFile(String dbCredentialFile) {
        this.dbCredentialFile = dbCredentialFile;
    }

    /**
     * Retrive user credentials.
     * If no credentals are stored, null will be returned.
     *
     * @param uid - user identifier.
     * @param siteKey - site credental identifier.
     * @return credentials
     */
    public UserSiteCredential getUserSiteCredential(String uid, String siteKey) {
        String sql = "select * from usersitecredential where uid = ? and sitekey = ?";

        RowMapper<UserSiteCredential> rowMapper = ParameterizedBeanPropertyRowMapper.newInstance(UserSiteCredential.class);
        UserSiteCredential creds = null;
        try {
            creds = getSimpleJdbcTemplate().queryForObject(sql, rowMapper, uid, siteKey);
            decryptSitePwd(creds);
        } catch (DataAccessException e) {
            log.info("Exception is leagal");
            return null;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        log.debug("User site credentials fetched from database: {}", creds);
        return creds;
    }

    /**
     * Store a credental.
     * Password will be encrypted before storage.
     *
     * @param siteCredential - credental to be stored
     */
    public void addUserSiteCredential(UserSiteCredential siteCredential) {
        UserSiteCredential copy = siteCredential.copy();
        encryptSitePwd(copy);

        String sql = insertOrUpdateSql(copy);
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(copy);

        getSimpleJdbcTemplate().update(sql, parameterSource);
    }

    private void decryptSitePwd(UserSiteCredential creds) throws GeneralSecurityException {
        String encryptedPwd = creds.getSitePassword();
        String clearPwd = null;
        clearPwd = cryptoUtils.decrypt(encryptedPwd);
        creds.setSitePassword(clearPwd);
    }

    private void encryptSitePwd(UserSiteCredential siteCredential) {
        try {
            String clearPwd = siteCredential.getSitePassword();
            String encryptedPwd = cryptoUtils.encrypt(clearPwd);
            siteCredential.setSitePassword(encryptedPwd);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private String insertOrUpdateSql(UserSiteCredential siteCredential) {
        String sql;
        if(siteUserExists(siteCredential)) {
            sql = "UPDATE USERSITECREDENTIAL "
                    + "SET SITEUSER = :siteUser,  SITEPASSWORD = :sitePassword "
                    + "WHERE UID = :uid AND SITEKEY = :siteKey";
        } else {
            sql = "INSERT INTO USERSITECREDENTIAL "
                    + "(UID, SITEKEY, SITEUSER, SITEPASSWORD) "
                    + "VALUES(:uid, :siteKey, :siteUser, :sitePassword)";
        }
        return sql;
    }

    private boolean siteUserExists(UserSiteCredential siteCredential) {
        String sql = "select count(*) from usersitecredential where uid = ? and sitekey = ?";

        return getSimpleJdbcTemplate().queryForInt(sql, siteCredential.getUid(), siteCredential.getSiteKey()) > 0;
    }
}
