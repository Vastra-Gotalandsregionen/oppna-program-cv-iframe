package se.vgregion.portal.iframe.repository;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.beanutils.BeanUtils;
import se.vgregion.portal.iframe.model.UserSiteCredential;
import se.vgregion.portal.iframe.util.CryptoUtils;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class JDBCCredentialVaultRepository extends SimpleJdbcDaoSupport implements CredentialVaultRepository {
    private static Logger log = LoggerFactory.getLogger(JDBCCredentialVaultRepository.class);
    private static String CREDENTIAL_VAULT_KEY_FILE = "/Users/david/AppServer/liferay-portal-5.2.3/tomcat-6.0.18/webapps/cv-iframe-portlet/WEB-INF/cv.key";


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
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (GeneralSecurityException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        log.debug("User site credentials fetched from database: {}", creds);
        return creds;
    }

    public void addUserSiteCredential(UserSiteCredential siteCredential) {
        encryptSitePwd(siteCredential);

        String sql = insertOrUpdateSql(siteCredential);
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(siteCredential);

        getSimpleJdbcTemplate().update(sql, parameterSource);
    }

    private void decryptSitePwd(UserSiteCredential creds) throws GeneralSecurityException, IOException {
        String encryptedPwd = creds.getSitePassword();
        String clearPwd = CryptoUtils.decrypt(encryptedPwd, new File(CREDENTIAL_VAULT_KEY_FILE));
        creds.setSitePassword(clearPwd);
    }

    private void encryptSitePwd(UserSiteCredential siteCredential) {
        try {
            String clearPwd = siteCredential.getSitePassword();
            String encryptedPwd = CryptoUtils.encrypt(clearPwd, new File(CREDENTIAL_VAULT_KEY_FILE));
            siteCredential.setSitePassword(encryptedPwd);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private String insertOrUpdateSql(UserSiteCredential siteCredential) {
        String sql;
        if(siteUserExists(siteCredential)) {
            sql = "UPDATE USERSITECREDENTIALS SET SITEUSER = :siteUser,  SITEPASSWORD = :sitePassword WHERE UID = :uid AND SITEKEY = :siteKey";
        } else {
            sql = "INSERT INTO USERSITECREDENTIAL (UID, SITEKEY, SITEUSER, SITEPASSWORD) VALUES(:uid, :siteKey, :siteUser, :sitePassword)";
        }
        return sql;
    }

    private boolean siteUserExists(UserSiteCredential siteCredential) {
        String sql = "select count(*) from usersitecredential where uid = ? and sitekey = ?";

        return getSimpleJdbcTemplate().queryForInt(sql, siteCredential.getUid(), siteCredential.getSiteKey()) > 0;
    }

    @Override
    protected JdbcTemplate createJdbcTemplate(DataSource dataSource) {
        Base64 base64 = new Base64();
        byte[] bytesArray = base64.decode("aGl0dGhlcm9hZA==".getBytes());
        try {
            BeanUtils.setProperty(dataSource, "password", new String(bytesArray));
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return super.createJdbcTemplate(dataSource);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
