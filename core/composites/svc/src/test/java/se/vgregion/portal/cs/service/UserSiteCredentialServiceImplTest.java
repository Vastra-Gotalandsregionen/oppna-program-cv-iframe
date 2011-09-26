/**
 * 
 */
package se.vgregion.portal.cs.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.domain.persistence.SiteKeyRepository;
import se.vgregion.portal.cs.domain.persistence.UserSiteCredentialRepository;

/**
 * @author anders.bergkvist@omegapoint.se
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class UserSiteCredentialServiceImplTest {

    CredentialServiceImpl credentialServiceImpl;

    @Mock
    UserSiteCredentialRepository userSiteCredentialRepository;

    @Mock
    SiteKeyRepository siteKeyRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        credentialServiceImpl = new CredentialServiceImpl();

        ReflectionTestUtils.setField(credentialServiceImpl, "userSiteCredentialRepository",
                userSiteCredentialRepository);
        ReflectionTestUtils.setField(credentialServiceImpl, "siteKeyRepository",
                siteKeyRepository);
    }

    @Test
    public final void testGetUserSiteCredential() {
        String uid = "screenName";
        String siteKey = "appName";
        UserSiteCredential expected = new UserSiteCredential();
        expected.setUid(uid);
        expected.setSiteKey(siteKey);
        expected.setId(Long.valueOf(1));

        when(userSiteCredentialRepository.getUserSiteCredential(anyString(), anyString())).thenReturn(expected);

        UserSiteCredential returned = credentialServiceImpl.getUserSiteCredential(uid, siteKey);
        assertEquals(uid, returned.getUid());
        assertEquals(siteKey, returned.getSiteKey());
        assertEquals(Long.valueOf(1), returned.getId());
    }

    @Test
    public final void testAddUserSiteCredential() {
        String uid = "screenName";
        String siteKey = "appName";

        UserSiteCredential create = new UserSiteCredential();
        create.setUid(uid);
        create.setSiteKey(siteKey);
        create.setId(Long.valueOf(1));

        credentialServiceImpl.save(create);

        verify(userSiteCredentialRepository).save(any(UserSiteCredential.class));
    }
}