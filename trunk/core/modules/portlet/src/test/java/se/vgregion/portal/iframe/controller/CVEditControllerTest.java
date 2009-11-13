package se.vgregion.portal.iframe.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Before;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.mock.web.portlet.MockActionRequest;
import org.springframework.ui.ModelMap;

import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import se.vgregion.portal.iframe.model.PortletConfig;

import java.io.IOException;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class CVEditControllerTest extends BastTestSetup {
    TestMockPortletPreferences prefs;
    ModelMap model = new ModelMap();

    @Before
    public void setup() throws ReadOnlyException {
        prefs = new TestMockPortletPreferences();
        initPortletPreferences(prefs);
    }

    @Test
    public void testEditPreferences() {
        CVEditController controller = new CVEditController();
        String result = controller.editPreferences(model, prefs);
        assertEquals("edit", result);

        PortletConfig portletConfig = (PortletConfig) model.get("portletConfig");
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

        assertEquals("apa", portletConfig.getHtmlAttribute("html1", ""));
        assertEquals("tomt", portletConfig.getHtmlAttribute("notThere", "tomt"));

    }

    @Test
    public void testSavePreferencesSecure() throws ValidatorException, IOException, ReadOnlyException {
        PortletConfig portletConfig = PortletConfig.getInstance(prefs);

        MockActionRequest actionRequest = new MockActionRequest();
        actionRequest.setSecure(true);

        CVEditController controller = new CVEditController();
        controller.savePreferences(actionRequest, prefs, portletConfig);

        assertEquals(1, prefs.getStoreCalled());
        assertEquals("https://test-src", portletConfig.getSrc());

        actionRequest.setSecure(false);
        controller.savePreferences(actionRequest, prefs, portletConfig);
        assertEquals("https://test-src", portletConfig.getSrc());
    }

    @Test
    public void testSavePreferencesNotSecure() throws ValidatorException, IOException, ReadOnlyException {
        PortletConfig portletConfig = PortletConfig.getInstance(prefs);

        MockActionRequest actionRequest = new MockActionRequest();
        actionRequest.setSecure(false);

        CVEditController controller = new CVEditController();
        controller.savePreferences(actionRequest, prefs, portletConfig);

        assertEquals(1, prefs.getStoreCalled());
        assertEquals("http://test-src", portletConfig.getSrc());

        actionRequest.setSecure(true);
        controller.savePreferences(actionRequest, prefs, portletConfig);
        assertEquals("http://test-src", portletConfig.getSrc());
    }

    @Test
    public void testSavePreferencesRelative() throws ValidatorException, IOException, ReadOnlyException {
        PortletConfig portletConfig = PortletConfig.getInstance(prefs);
        portletConfig.setSrc("/relative");

        MockActionRequest actionRequest = new MockActionRequest();
        actionRequest.setSecure(false);

        CVEditController controller = new CVEditController();
        controller.savePreferences(actionRequest, prefs, portletConfig);

        assertEquals(1, prefs.getStoreCalled());
        assertEquals("/relative", portletConfig.getSrc());

        actionRequest.setSecure(true);

        controller.savePreferences(actionRequest, prefs, portletConfig);

        assertEquals("/relative", portletConfig.getSrc());
    }

    class TestMockPortletPreferences extends MockPortletPreferences {
        int storeCalled = 0;

        public int getStoreCalled() {
            return storeCalled;
        }


        @Override
        public void store() throws IOException, ValidatorException {
            storeCalled++;
            super.store();
        }
    }

}
