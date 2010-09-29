package se.vgregion.portal.csiframe.domain.persistence.jpa;

import java.security.GeneralSecurityException;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import se.vgregion.portal.core.infrastructure.persistence.jpa.DefaultJpaRepository;
import se.vgregion.portal.csiframe.domain.UserSiteCredential;
import se.vgregion.portal.csiframe.domain.UserSiteCredentialRepository;
import se.vgregion.portal.csiframe.util.CryptoUtil;

/**
 * This action do that and that, if it has something special it is.
 * 
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Repository
public class JpaUserSiteCredentialRepository extends DefaultJpaRepository<UserSiteCredential> implements
        UserSiteCredentialRepository {

    @Autowired
    private CryptoUtil cryptoUtils;

    public void setCryptoUtils(CryptoUtil cryptoUtils) {
        this.cryptoUtils = cryptoUtils;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserSiteCredential getUserSiteCredential(String uid, String siteKey) {
        String queryString = "SELECT s FROM UserSiteCredential s WHERE s.uid = :uid and s.siteKey = :siteKey";
        Query query = entityManager.createQuery(queryString).setParameter("uid", uid)
                .setParameter("siteKey", siteKey);
        @SuppressWarnings("unchecked")
        List<UserSiteCredential> results = query.getResultList();
        if (results.size() == 0) {
            return null;
        }
        if (results.size() > 1) {
            String message = "Argument uid=" + uid + " and siteKey=" + siteKey
                    + " did not match a unique post of the type " + UserSiteCredential.class.getSimpleName();
            throw new IllegalArgumentException(message);
        }
        UserSiteCredential result = results.get(0);
        try {
            decryptSitePwd(result);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Failed to decrypt password", e);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addUserSiteCredential(UserSiteCredential siteCredential) {
        encryptSitePwd(siteCredential);
        if (siteCredential.getId() == null) {
            entityManager.persist(siteCredential);
        } else {
            entityManager.merge(siteCredential);
        }
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

}
