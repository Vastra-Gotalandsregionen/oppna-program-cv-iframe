package se.vgregion.portal.iframe;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class BaseTestSetup {
    protected void initPortletPreferences(PortletPreferences prefs) throws ReadOnlyException {
        prefs.setValue("site-key", "test-key");
        prefs.setValue("src", "test-src");
        prefs.setValue("relative", "false");
        prefs.setValue("auth", "true");
        prefs.setValue("auth-type", "form"); // basic or form
        prefs.setValue("form-method", "post"); // get or post
        prefs.setValue("user-name-field", "username");
        prefs.setValue("password-field", "password");
        prefs.setValue("hidden-variables", "test1=hidden1&test2=hidden2");
        prefs.setValue("html-attributes", "html1=apa\nhtml2=bepa");
    }
}
