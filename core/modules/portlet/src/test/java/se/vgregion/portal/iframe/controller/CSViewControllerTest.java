package se.vgregion.portal.iframe.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.mock.web.portlet.*;
import org.springframework.ui.ModelMap;
import se.vgregion.portal.iframe.model.PortletConfig;
import se.vgregion.portal.iframe.model.*;
import se.vgregion.portal.repository.CredentialStoreRepository;

import javax.portlet.*;
import java.util.Map;
import java.util.HashMap;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class CSViewControllerTest extends BastTestSetup {
    
    CSViewController controller;

    @Before
    public void setUp() {
        controller = new CSViewController();
        controller.setCredentialStoreRepository(new TestStubCredentialStoreRepository());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testShowView() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);

        RenderRequest mockReq = new MockRenderRequest(PortletMode.VIEW);
        RenderResponse mockResp = new MockRenderResponse();
        ModelMap model = new ModelMap();

        String response = controller.showView(prefs, mockReq, mockResp, model);
        assertEquals("userCredentialForm", response);
    }

    @Test
    public void testShowView_NoAuth() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);
        prefs.setValue("auth", "false");

        RenderRequest mockReq = new MockRenderRequest(PortletMode.VIEW);
        RenderResponse mockResp = new MockRenderResponse();
        ModelMap model = new ModelMap();

        String response = controller.showView(prefs, mockReq, mockResp, model);
        assertEquals("view", response);

        // Test model
        assertEquals("test-src", model.get("iFrameSrc"));
        assertEquals("test-src", model.get("baseSrc"));
        assertEquals("300", model.get("iFrameHeight"));
        assertEquals("0", model.get("border"));
        assertEquals("#000000", model.get("bordercolor"));
        assertEquals("0", model.get("frameborder"));
        assertEquals("600", model.get("height-maximized"));
        assertEquals("300", model.get("height-normal"));
        assertEquals("0", model.get("hspace"));
        assertEquals("auto", model.get("scrolling"));
        assertEquals("0", model.get("vspace"));
        assertEquals("100%", model.get("width"));
    }

    @Test
    public void testShowView_AuthNoSiteUser() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);
        prefs.setValue("auth", "true");

        RenderRequest mockReq = new MockRenderRequest(PortletMode.VIEW);
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString(), "no-credential-user");
        mockReq.setAttribute(PortletRequest.USER_INFO, userInfo);

        RenderResponse mockResp = new MockRenderResponse();
        ModelMap model = new ModelMap();

        String response = controller.showView(prefs, mockReq, mockResp, model);
        assertEquals("userCredentialForm", response);
    }

    @Test
    public void testShowView_AuthSiteUserExistWrongSiteKey() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);
        prefs.setValue("auth", "true");

        RenderRequest mockReq = (RenderRequest) initPortletRequest(new MockRenderRequest());

        RenderResponse mockResp = new MockRenderResponse();
        ModelMap model = new ModelMap();

        String response = controller.showView(prefs, mockReq, mockResp, model);
        assertEquals("userCredentialForm", response);
    }

    @Test
    public void testShowView_AuthSiteUserExistSiteKeyExistBasic() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);
        prefs.setValue("src", "http://www.google.com");
        prefs.setValue("auth", "true");
        prefs.setValue("auth-type", "basic");
        prefs.setValue("site-key", "test-site-key");

        RenderRequest mockReq = (RenderRequest) initPortletRequest(new MockRenderRequest());

        RenderResponse mockResp = new MockRenderResponse();
        ModelMap model = new ModelMap();

        String response = controller.showView(prefs, mockReq, mockResp, model);
        assertEquals("view", response);

        UserSiteCredential siteCredential = (UserSiteCredential) model.get("siteCredential");
        assertEquals("test-user", siteCredential.getUid());
        assertEquals("test-site-key", siteCredential.getSiteKey());
        assertEquals("test-site-user", siteCredential.getSiteUser());
        assertEquals("test-site-password", siteCredential.getSitePassword());

        // Test model
        assertEquals("http://"+siteCredential.getSiteUser()+":"+siteCredential.getSitePassword()+"@www.google.com", model.get("iFrameSrc"));
        assertEquals("http://"+siteCredential.getSiteUser()+":"+siteCredential.getSitePassword()+"@www.google.com", model.get("baseSrc"));
        assertEquals("300", model.get("iFrameHeight"));
        assertEquals("0", model.get("border"));
        assertEquals("#000000", model.get("bordercolor"));
        assertEquals("0", model.get("frameborder"));
        assertEquals("600", model.get("height-maximized"));
        assertEquals("300", model.get("height-normal"));
        assertEquals("0", model.get("hspace"));
        assertEquals("auto", model.get("scrolling"));
        assertEquals("0", model.get("vspace"));
        assertEquals("100%", model.get("width"));
    }

    @Test
    public void testShowView_AuthSiteUserExistSiteKeyExistForm() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);
        prefs.setValue("src", "http://www.google.com");
        prefs.setValue("auth", "true");
        prefs.setValue("auth-type", "form");
        prefs.setValue("site-key", "test-site-key");

        MockRenderRequest mockReq = new MockRenderRequest(PortletMode.VIEW);
        RenderRequest req = (MockRenderRequest) initPortletRequest(mockReq);

        MockRenderResponse resp = new TestStubMockRenderResponse();

        ModelMap model = new ModelMap();

        String response = controller.showView(prefs, req, resp, model);
        assertEquals("view", response);

        UserSiteCredential siteCredential = (UserSiteCredential) model.get("siteCredential");
        assertEquals("test-user", siteCredential.getUid());
        assertEquals("test-site-key", siteCredential.getSiteKey());
        assertEquals("test-site-user", siteCredential.getSiteUser());
        assertEquals("test-site-password", siteCredential.getSitePassword());

        // Test model
        assertEquals("http://localhost/mockportlet?resourceID=resourceId", model.get("iFrameSrc"));
        assertEquals("http://localhost/", model.get("baseSrc"));
        assertEquals("300", model.get("iFrameHeight"));
        assertEquals("0", model.get("border"));
        assertEquals("#000000", model.get("bordercolor"));
        assertEquals("0", model.get("frameborder"));
        assertEquals("600", model.get("height-maximized"));
        assertEquals("300", model.get("height-normal"));
        assertEquals("0", model.get("hspace"));
        assertEquals("auto", model.get("scrolling"));
        assertEquals("0", model.get("vspace"));
        assertEquals("100%", model.get("width"));
    }

    @Test
    public void testShowView_WindowStateMaximized() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);
        prefs.setValue("src", "http://www.google.com");
        prefs.setValue("auth", "true");
        prefs.setValue("auth-type", "form");
        prefs.setValue("site-key", "test-site-key");

        MockRenderRequest mockReq = new MockRenderRequest(PortletMode.VIEW);
        mockReq = (MockRenderRequest)initPortletRequest(mockReq);

        MockRenderResponse mockResp = new TestStubMockRenderResponse();
        mockResp.createResourceURL();

        ModelMap model = new ModelMap();

        // NORMAL
        mockReq.setWindowState(WindowState.NORMAL);
        controller.showView(prefs, mockReq, mockResp, model);
        assertEquals("300", model.get("iFrameHeight"));

        // MAXIMIZED
        mockReq.setWindowState(WindowState.MAXIMIZED);
        controller.showView(prefs, mockReq, mockResp, model);
        assertEquals("600", model.get("iFrameHeight"));
    }

    private PortletRequest initPortletRequest(PortletRequest req) {
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString(), "test-user");
        req.setAttribute(PortletRequest.USER_INFO, userInfo);
        return req;
    }

    @Test
    public void testChangeVaultCredentials_NoUserSiteCredential() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);

        MockRenderRequest mockReq = new MockRenderRequest(PortletMode.VIEW);
        RenderRequest req = (RenderRequest)initPortletRequest(mockReq);
        ModelMap model = new ModelMap();

        String response = controller.changeVaultCredentials(prefs, req, model);
        assertEquals("userCredentialForm", response);

        PortletConfig portletConfig = (se.vgregion.portal.iframe.model.PortletConfig) model.get("portletConfig");
        assertNotNull(portletConfig);
        assertEquals("test-key", portletConfig.getSiteKey());
        assertEquals("test-src", portletConfig.getSrc());
        assertEquals(Boolean.FALSE, portletConfig.isRelative());
        assertEquals(Boolean.TRUE, portletConfig.isAuth());
        assertEquals("form", portletConfig.getAuthType());
        assertEquals("post", portletConfig.getFormMethod());
        assertEquals("username", portletConfig.getSiteUserNameField());
        assertEquals("password", portletConfig.getSitePasswordField());
        assertEquals("test1=hidden1;test2=hidden2", portletConfig.getHiddenVariables());
        assertEquals("html1=apa\nhtml2=bepa", portletConfig.getHtmlAttributes());

        UserSiteCredential siteCredential = (UserSiteCredential) model.get("siteCredential");
        assertNotNull(siteCredential);

    }

    @Test
    public void testChangeVaultCredentials_NoAuth() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);
        prefs.setValue("auth", "false");

        MockRenderRequest mockReq = new MockRenderRequest(PortletMode.VIEW);
        RenderRequest req = (RenderRequest)initPortletRequest(mockReq);
        ModelMap model = new ModelMap();

        String response = controller.changeVaultCredentials(prefs, req, model);
        assertEquals("view", response);

        PortletConfig portletConfig = (PortletConfig) model.get("portletConfig");
        assertNotNull(portletConfig);
        assertEquals("test-src", portletConfig.getSrc());
        assertEquals(Boolean.FALSE, portletConfig.isRelative());
        assertEquals(Boolean.FALSE, portletConfig.isAuth());
        assertEquals("html1=apa\nhtml2=bepa", portletConfig.getHtmlAttributes());
    }

    @Test
    public void testShowProxyForm_AuthForm() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);

        ResourceRequest mockReq = new MockResourceRequest();
        ResourceRequest req = (ResourceRequest) initPortletRequest(mockReq);

        ModelMap model = new ModelMap();

        String result = controller.showProxyForm(prefs, req, model);

        assertEquals("proxyLoginForm", result);

        PortletConfig portletConfig = (PortletConfig) model.get("portletConfig");
        assertNotNull(portletConfig);

        UserSiteCredential siteCredential = (UserSiteCredential)model.get("siteCredential");
        assertNotNull(siteCredential);
    }

    @Test
    public void testShowProxyForm_NoAuthForm() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);
        prefs.setValue("auth", "false");

        ResourceRequest mockReq = new MockResourceRequest();
        ResourceRequest req = (ResourceRequest) initPortletRequest(mockReq);

        ModelMap model = new ModelMap();

        String result = controller.showProxyForm(prefs, req, model);

        assertEquals("view", result);
    }

    @Test
    public void testShowProxyForm_AuthNoForm() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);
        prefs.setValue("auth-type", "basic");

        ResourceRequest mockReq = new MockResourceRequest();
        ResourceRequest req = (ResourceRequest) initPortletRequest(mockReq);

        ModelMap model = new ModelMap();

        String result = controller.showProxyForm(prefs, req, model);

        assertEquals("view", result);
    }

    @Test
    public void testShowProxyForm_NoAuthNoForm() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);
        prefs.setValue("auth", "false");
        prefs.setValue("auth-type", "basic");

        ResourceRequest mockReq = new MockResourceRequest();
        ResourceRequest req = (ResourceRequest) initPortletRequest(mockReq);

        ModelMap model = new ModelMap();

        String result = controller.showProxyForm(prefs, req, model);

        assertEquals("view", result);
    }

    @Test
    public void testStoreUserCredential() {
        // TODO: How test this?
    }

    class TestStubCredentialStoreRepository implements CredentialStoreRepository {
       
        public UserSiteCredential getUserSiteCredential(String uid, String siteKey) {
            if ("test-user".equals(uid) && "test-site-key".equals(siteKey)) {
                UserSiteCredential siteCredential = new UserSiteCredential("test-user", "test-site-key");
                siteCredential.setSiteUser("test-site-user");
                siteCredential.setSitePassword("test-site-password");

                return siteCredential;
            } else {
                return null;
            }
        }

        public void addUserSiteCredential(UserSiteCredential siteCredential) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    class TestStubMockRenderResponse extends MockRenderResponse {
        @Override
        public ResourceURL createResourceURL() {
            MockResourceURL mockResourceURL = new MockResourceURL();
            mockResourceURL.setResourceID("resourceId");

            return mockResourceURL;
        }
    }
}
