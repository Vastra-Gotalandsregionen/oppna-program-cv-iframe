package se.vgregion.portal.admin.controller;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.service.CredentialService;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;

import static javax.portlet.PortletRequest.P3PUserInfos.USER_LOGIN_ID;
import static org.mockito.Mockito.*;

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class ViewControllerTest {

    ViewController viewController;

    @Mock
    CredentialService credentialService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        viewController = new ViewController();

        ReflectionTestUtils.setField(viewController, "credentialService", credentialService);
    }

    @Test
    public void testShowView() throws Exception {
        RenderRequest request = mock(RenderRequest.class);
        Model model = mock(Model.class);

        when(request.getAttribute(PortletRequest.USER_INFO))
                .thenReturn(ImmutableMap.of(USER_LOGIN_ID.toString(), "uid"));

        UserSiteCredential usc1 = new UserSiteCredential("uid", "key1");
        UserSiteCredential usc2 = new UserSiteCredential("uid", "key2");
        UserSiteCredential usc3 = new UserSiteCredential("uid", "key4");
        when(credentialService.getAllSiteCredentials("uid")).thenReturn(Lists.newArrayList(usc1, usc2, usc3));

        SiteKey sk1 = new SiteKey("key1", "Key1", "", false, false, false);
        SiteKey sk2 = new SiteKey("key2", "Key2", "", false, false, true);
        SiteKey sk3 = new SiteKey("key3", "Key3", "", false, false, true);
        SiteKey sk4 = new SiteKey("key4", "iKey4", "", false, false, true);
        when(credentialService.getAllSiteKeys()).thenReturn(Lists.newArrayList(sk1, sk2, sk3, sk4));

        String result = viewController.showView(request, model);

        Assert.assertEquals("view", result);
        verify(model).addAttribute("userCredentials", Lists.newArrayList(
                new CredentialSiteKeyFormBean(usc3, sk4),
                new CredentialSiteKeyFormBean(usc2, sk2)
        ));
    }

    @Test
    public void testUpdateCredential_Ok() throws Exception {
        UserSiteCredential credential = new UserSiteCredential("uid", "siteKey");
        Model model = mock(Model.class);

        viewController.updateCredential(credential, model);

        verify(credentialService).save(credential);
        verify(model).addAttribute("saveAction", "siteKey");
    }

    @Test
    public void testUpdateCredential_Fail() throws Exception {
        UserSiteCredential credential = new UserSiteCredential("uid", "siteKey");
        Model model = mock(Model.class);

        doThrow(new RuntimeException()).when(credentialService).save(credential);

        viewController.updateCredential(credential, model);

        verify(credentialService).save(credential);
        verify(model).addAttribute("saveActionFailed", "siteKey");
    }

    @Test
    public void testRefreshCredential() throws Exception {
        // has no side effects, just a target
        viewController.refreshCredential();
    }

    @Test
    public void testDeleteCredentials_Ok() throws Exception {
        UserSiteCredential credential = new UserSiteCredential("uid", "siteKey");
        Model model = mock(Model.class);

        when(credentialService.getUserSiteCredential(0L)).thenReturn(credential);

        viewController.deleteCredentials(0L, model);

        verify(credentialService).removeUserSiteCredential(0L);
        verify(model).addAttribute("removeAction", "siteKey");
    }

    @Test
    public void testDeleteCredentials_Fail() throws Exception {
        UserSiteCredential credential = new UserSiteCredential("uid", "siteKey");
        Model model = mock(Model.class);

        when(credentialService.getUserSiteCredential(0L)).thenReturn(credential);

        doThrow(new RuntimeException()).when(credentialService).removeUserSiteCredential(0L);

        viewController.deleteCredentials(0L, model);

        verify(credentialService).removeUserSiteCredential(0L);
        verify(model).addAttribute("removeActionFailed", "siteKey");
    }

    @Test
    public void testDeleteCredentialsFail2() throws Exception {
        UserSiteCredential credential = new UserSiteCredential("uid", "siteKey");
        Model model = mock(Model.class);

        when(credentialService.getUserSiteCredential(0L)).thenReturn(null);

        doThrow(new RuntimeException()).when(credentialService).removeUserSiteCredential(0L);

        viewController.deleteCredentials(0L, model);

        verify(credentialService).removeUserSiteCredential(0L);
        verify(model).addAttribute("removeActionFailed", true);
    }
}
