package se.vgregion.portal.cs.migration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.domain.persistence.UserSiteCredentialRepository;
import se.vgregion.portal.cs.util.AesCtrCryptoUtilImpl;
import se.vgregion.portal.cs.util.CryptoUtilImpl;

import javax.annotation.Resource;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * @author Patrik Bergström
 */
public class MigrationService {

    private static final Logger LOGGER = Logger.getLogger(MigrationService.class.getName());

    @Value("${keyFilePath}")
    private String keyFilePath;// = "security/cv.key";

    @Autowired
    private UserSiteCredentialRepository repository;
    @Resource(name = "cryptoUtil")
    private CryptoUtilImpl cryptoUtil;
    // AesCtrCryptoUtilImpl is not created by Spring context since two CryptoUtil's cause problems to the
    // autowiring (which we don't want to change just because of this migration application)
    private AesCtrCryptoUtilImpl aesCtrCryptoUtil;
    final String pathname = "newCv.key"; //default access for the tests

    public void setAesCtrCryptoUtil(AesCtrCryptoUtilImpl aesCtrCryptoUtil) {
        this.aesCtrCryptoUtil = aesCtrCryptoUtil;
    }

    @Transactional
    public void migrateEcbToCtr() {
        Collection<UserSiteCredential> all = findAll();
        for (UserSiteCredential usc : all) {
            String decrypt = null;
            try {
                decrypt = cryptoUtil.decrypt(usc.getSitePassword());
                String ctrEncrypt = aesCtrCryptoUtil.encrypt(decrypt);
                usc.setSitePassword(ctrEncrypt);
                merge(usc);
                LOGGER.info("CTR encrypted: " + ctrEncrypt + " - saved.");
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Transactional
    public void migrateCtr2Ecb() {
        Collection<UserSiteCredential> all = findAll();
        for (UserSiteCredential usc : all) {
            String decrypt = null;
            try {
                decrypt = aesCtrCryptoUtil.decrypt(usc.getSitePassword());
                String ecbEncrypt = cryptoUtil.encrypt(decrypt);
                usc.setSitePassword(ecbEncrypt);
                merge(usc);
                LOGGER.info("ECB encrypted: " + ecbEncrypt + " - saved.");
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public File migrateAndUpdateKey() {
        AesCtrCryptoUtilImpl aesCtrCryptoUtilNew = new AesCtrCryptoUtilImpl();
        File newKeyFile = new File(pathname);
        aesCtrCryptoUtilNew.setKeyFile(newKeyFile);
        Collection<UserSiteCredential> all = findAll();
        for (UserSiteCredential usc : all) {
            String decrypt = null;
            try {
                decrypt = aesCtrCryptoUtil.decrypt(usc.getSitePassword());
                String ecbEncrypt = aesCtrCryptoUtilNew.encrypt(decrypt);
                usc.setSitePassword(ecbEncrypt);
                merge(usc);
                LOGGER.info("New ECB encrypted: " + ecbEncrypt + " - saved.");
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
        return newKeyFile;
    }

    @Transactional
    public void undoMigrateAndUpdateKey() {
        AesCtrCryptoUtilImpl aesCtrCryptoUtilNew = new AesCtrCryptoUtilImpl();
        File newKeyFile = new File(pathname);
        aesCtrCryptoUtilNew.setKeyFile(newKeyFile);
        Collection<UserSiteCredential> all = findAll();
        for (UserSiteCredential usc : all) {
            String decrypt = null;
            try {
                decrypt = aesCtrCryptoUtilNew.decrypt(usc.getSitePassword());
                String ecbEncrypt = aesCtrCryptoUtil.encrypt(decrypt);
                usc.setSitePassword(ecbEncrypt);
                merge(usc);
                LOGGER.info("Old ECB encrypted: " + ecbEncrypt + " - saved.");
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void merge(UserSiteCredential credential) {
        repository.merge(credential);
    }

    public Collection<UserSiteCredential> findAll() {
        return repository.findAll();
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

}
