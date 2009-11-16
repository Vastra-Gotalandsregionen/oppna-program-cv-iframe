package se.vgregion.portal.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.portal.iframe.model.UserSiteCredential;
import se.vgregion.portal.iframe.util.CryptoUtil;

import java.security.GeneralSecurityException;

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class JDBCCredentialVaultRepositoryTest {

    @Autowired
    private JDBCCredentialVaultRepository repo;

    @Before
    public void setup() {
        repo.setCryptoUtils(new CryptoUtil() {
            public String encrypt(String value) throws GeneralSecurityException {
                return new StringBuffer(value).reverse().toString();
            }

            public String decrypt(String value) throws GeneralSecurityException {
                return new StringBuffer(value).reverse().toString();
            }
        });
    }

    @Test
    public void testGetUserSiteCredential() {
        UserSiteCredential sc1 = repo.getUserSiteCredential("no-test-uid", "test-key");
        assertNull(sc1);

        UserSiteCredential sc2 = repo.getUserSiteCredential("test-uid", "test-key");
        assertNotNull(sc2);
    }

    @Test
    public void testAddUserSiteCredential() {
        UserSiteCredential siteCredential = new UserSiteCredential("new-test-uid", "new-site-key");
        siteCredential.setSiteUser("new-site-user");
        siteCredential.setSitePassword("new-site-password");

        repo.addUserSiteCredential(siteCredential);

        checkCredential(siteCredential);

        siteCredential.setSitePassword("changed-pw");
        repo.addUserSiteCredential(siteCredential);

        checkCredential(siteCredential);
    }

    private void checkCredential(UserSiteCredential siteCredential) {
        UserSiteCredential response = repo.getUserSiteCredential("new-test-uid", "new-site-key");

        assertEquals(siteCredential, response);
        assertEquals(siteCredential.getUid(), response.getUid());
        assertEquals(siteCredential.getSiteKey(), response.getSiteKey());
        assertEquals(siteCredential.getSiteUser(), response.getSiteUser());
        assertEquals(siteCredential.getSitePassword(), response.getSitePassword());
    }
}
