/**
 * 
 */
package se.vgregion.portal.csiframe.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import se.vgregion.portal.csiframe.domain.UserSiteCredential;
import se.vgregion.portal.csiframe.domain.persistence.UserSiteCredentialRepository;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class UserSiteCredentialServiceImplTest {

    CredentialServiceImpl userSiteCredentialServiceImpl;

    @Mock
    UserSiteCredentialRepository userSiteCredentialRepository;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userSiteCredentialServiceImpl = new CredentialServiceImpl();

        ReflectionTestUtils.setField(userSiteCredentialServiceImpl, "userSiteCredentialRepository",
                userSiteCredentialRepository);
    }

    /**
     * Test method for
     * {@link CredentialServiceImpl#getUserSiteCredential(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testGetUserSiteCredential() {
        String uid = "screenName";
        String siteKey = "appName";
        UserSiteCredential expected = new UserSiteCredential();
        expected.setUid(uid);
        expected.setSiteKey(siteKey);
        expected.setId(Long.valueOf(1));

        when(userSiteCredentialRepository.getUserSiteCredential(anyString(), anyString())).thenReturn(expected);

        UserSiteCredential returned = userSiteCredentialServiceImpl.getUserSiteCredential(uid, siteKey);
        assertEquals(uid, returned.getUid());
        assertEquals(siteKey, returned.getSiteKey());
        assertEquals(Long.valueOf(1), returned.getId());
    }

    /**
     * Test method for
     * {@link CredentialServiceImpl#save(se.vgregion.portal.csiframe.domain.UserSiteCredential)}
     * .
     */
    @Test
    public final void testAddUserSiteCredential() {
        String uid = "screenName";
        String siteKey = "appName";

        UserSiteCredential create = new UserSiteCredential();
        create.setUid(uid);
        create.setSiteKey(siteKey);
        create.setId(Long.valueOf(1));

        userSiteCredentialServiceImpl.save(create);

        verify(userSiteCredentialRepository).save(any(UserSiteCredential.class));
    }
}
