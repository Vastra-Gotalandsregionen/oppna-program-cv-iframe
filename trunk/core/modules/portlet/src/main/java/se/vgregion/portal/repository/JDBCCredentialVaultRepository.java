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
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class JDBCCredentialVaultRepository extends SimpleJdbcDaoSupport implements CredentialVaultRepository {
    private static Logger log = LoggerFactory.getLogger(JDBCCredentialVaultRepository.class);

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
            sql = "UPDATE USERSITECREDENTIAL SET SITEUSER = :siteUser,  SITEPASSWORD = :sitePassword WHERE UID = :uid AND SITEKEY = :siteKey";
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
        try {
            log.debug("username: {}",BeanUtils.getProperty(dataSource, "username"));
            log.debug("password: {}",BeanUtils.getProperty(dataSource, "password"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return super.createJdbcTemplate(dataSource);
//        Properties dbCredentials = new Properties();
//        try {
//            dbCredentials.load(new FileReader(dbCredentialFile));
//        } catch (IOException e) {
//            // RuntimeException will be thrown
//            dbCredentialFileNotConfigured(dbCredentials);
//        }
//
//        setDbUserCredential(dataSource, dbCredentials);
//
//        return super.createJdbcTemplate(dataSource);
    }

    private void setDbUserCredential(DataSource dataSource, Properties dbCredentials) {
        String user = dbCredentials.getProperty("user");
        String encryptedPwd = dbCredentials.getProperty("pwd");

        String clearPwd = null;
        try {
            clearPwd = cryptoUtils.decrypt(encryptedPwd);

            BeanUtils.setProperty(dataSource, "username", user);
            BeanUtils.setProperty(dataSource, "password", clearPwd);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("The database login-credential has to be configured", e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void dbCredentialFileNotConfigured(Properties dbCredentials) {
        dbCredentials.setProperty("user", "<db-username>");
        dbCredentials.setProperty("password", "<encrypted db-password> - use your cv.key to encrypt the password");
        try {
            dbCredentials.store(new FileWriter(dbCredentialFile), "");
        } catch (IOException e1) {
            log.error("Could not access database login-credential properties file {}", dbCredentialFile);
        }
        throw new RuntimeException("The database login-credential properties file has to be configured");
    }
}
