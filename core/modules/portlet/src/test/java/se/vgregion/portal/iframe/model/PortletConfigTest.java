package se.vgregion.portal.iframe.model;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.springframework.mock.web.portlet.MockPortletPreferences;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;

import se.vgregion.portal.iframe.BaseTestSetup;

import java.util.Map;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class PortletConfigTest extends BaseTestSetup {
    @Test
    public void testGetHiddenVarialbleMap() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);

        PortletConfig portletConfig = PortletConfig.getInstance(prefs);

        Map<String, String> hiddenVars = portletConfig.getHiddenVarialbleMap();
        assertEquals(2, hiddenVars.size());
        assertEquals("hidden1", hiddenVars.get("test1"));
        assertEquals("hidden2", hiddenVars.get("test2"));
    }

    @Test
    public void testGetHiddenVarialbleMap_NoValue() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);
        prefs.setValue("hidden-variables", "test1&test2=hidden2&test3&test4=hidden4&test5");

        PortletConfig portletConfig = PortletConfig.getInstance(prefs);

        Map<String, String> hiddenVars = portletConfig.getHiddenVarialbleMap();
        assertEquals(5, hiddenVars.size());
        assertEquals("", hiddenVars.get("test1"));
        assertEquals("hidden2", hiddenVars.get("test2"));
        assertEquals("", hiddenVars.get("test3"));
        assertEquals("hidden4", hiddenVars.get("test4"));
        assertEquals("", hiddenVars.get("test5"));
    }

    @Test
    public void testGetHiddenVarialbleMap_EqualsInValue() throws ReadOnlyException {
        PortletPreferences prefs = new MockPortletPreferences();
        initPortletPreferences(prefs);
        prefs.setValue("hidden-variables", "test1==hidden1&test2=hidden2=&test3=hid=den3&test4===hid=den4=");

        PortletConfig portletConfig = PortletConfig.getInstance(prefs);

        Map<String, String> hiddenVars = portletConfig.getHiddenVarialbleMap();
        assertEquals(4, hiddenVars.size());
        assertEquals("=hidden1", hiddenVars.get("test1"));
        assertEquals("hidden2=", hiddenVars.get("test2"));
        assertEquals("hid=den3", hiddenVars.get("test3"));
        assertEquals("==hid=den4=", hiddenVars.get("test4"));
    }
}