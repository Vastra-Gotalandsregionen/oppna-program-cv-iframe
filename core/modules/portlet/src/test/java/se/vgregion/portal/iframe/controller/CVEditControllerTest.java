package se.vgregion.portal.iframe.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.mock.web.portlet.MockActionRequest;
import org.springframework.ui.ModelMap;

import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import se.vgregion.portal.iframe.model.Credential;

import java.io.IOException;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({})
public class CVEditControllerTest {
    TestMockPortletPreferences prefs;
    ModelMap model = new ModelMap();

    @Before
    public void setup() throws ReadOnlyException {
        prefs = new TestMockPortletPreferences();
        prefs.setValue("site-key", "test-key");
        prefs.setValue("src", "test-src");
        prefs.setValue("relative", "false");
        prefs.setValue("auth", "true");
        prefs.setValue("auth-type", "form"); // basic or form
        prefs.setValue("form-method", "post"); // get or post
        prefs.setValue("user-name-field", "username");
        prefs.setValue("password-field", "password");
        prefs.setValue("hidden-variables", "test1=hidden1;test2=hidden2");
        prefs.setValue("html-attributes", "html1=apa\nhtml2=bepa");
    }

    @Test
    public void testEditPreferences() {
        CVEditController controller = new CVEditController();
        String result = controller.editPreferences(model, prefs);
        assertEquals("edit", result);

        Credential credential = (Credential) model.get("credential");
        assertEquals("test-key", credential.getSiteKey());
        assertEquals("test-src", credential.getSrc());
        assertEquals(Boolean.FALSE, credential.isRelative());
        assertEquals(Boolean.TRUE, credential.isAuth());
        assertEquals("form", credential.getAuthType());
        assertEquals("post", credential.getFormMethod());
        assertEquals("username", credential.getSiteUserNameField());
        assertEquals("password", credential.getSitePasswordField());
        assertEquals("test1=hidden1;test2=hidden2", credential.getHiddenVariables());
        assertEquals("html1=apa\nhtml2=bepa", credential.getHtmlAttributes());

        assertEquals("apa", credential.getHtmlAttribute("html1", ""));
        assertEquals("tomt", credential.getHtmlAttribute("notThere", "tomt"));

    }

    @Test
    public void testSavePreferencesSecure() throws ValidatorException, IOException, ReadOnlyException {
        Credential credential = Credential.getInstance(prefs);

        MockActionRequest actionRequest = new MockActionRequest();
        actionRequest.setSecure(true);

        CVEditController controller = new CVEditController();
        controller.savePreferences(actionRequest, prefs, credential);

        assertEquals(1, prefs.getStoreCalled());
        assertEquals("https://test-src", credential.getSrc());

        actionRequest.setSecure(false);
        controller.savePreferences(actionRequest, prefs, credential);
        assertEquals("https://test-src", credential.getSrc());
    }

    @Test
    public void testSavePreferencesNotSecure() throws ValidatorException, IOException, ReadOnlyException {
        Credential credential = Credential.getInstance(prefs);

        MockActionRequest actionRequest = new MockActionRequest();
        actionRequest.setSecure(false);

        CVEditController controller = new CVEditController();
        controller.savePreferences(actionRequest, prefs, credential);

        assertEquals(1, prefs.getStoreCalled());
        assertEquals("http://test-src", credential.getSrc());

        actionRequest.setSecure(true);
        controller.savePreferences(actionRequest, prefs, credential);
        assertEquals("http://test-src", credential.getSrc());
    }

    @Test
    public void testSavePreferencesRelative() throws ValidatorException, IOException, ReadOnlyException {
        Credential credential = Credential.getInstance(prefs);
        credential.setSrc("/relative");

        MockActionRequest actionRequest = new MockActionRequest();
        actionRequest.setSecure(false);

        CVEditController controller = new CVEditController();
        controller.savePreferences(actionRequest, prefs, credential);

        assertEquals(1, prefs.getStoreCalled());
        assertEquals("/relative", credential.getSrc());

        actionRequest.setSecure(true);

        controller.savePreferences(actionRequest, prefs, credential);

        assertEquals("/relative", credential.getSrc());
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
