/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.portal.iframe.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.portlet.MockActionRequest;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.ui.ModelMap;
import se.vgregion.portal.iframe.BaseTestSetup;
import se.vgregion.portal.iframe.model.PortletConfig;
import se.vgregion.portal.iframe.util.LoginScreenScraper;

import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class CSEditControllerTest extends BaseTestSetup {
    TestMockPortletPreferences stubPrefs;
    ModelMap model = new ModelMap();
    LoginScreenScraper stubLoginScreenScraper;

    @Before
    public void setup() throws ReadOnlyException {
        stubPrefs = new TestMockPortletPreferences();
        initPortletPreferences(stubPrefs);
        stubLoginScreenScraper = new LoginScreenScraper() {
            public void advancedScraping(PortletConfig portletConfig) {
                // nothing for now
            }
        };
    }

    @Test
    public void testEditPreferences() {
        CSEditController controller = new CSEditController();
        controller.setLoginScreenScraper(stubLoginScreenScraper);
        String result = controller.editPreferences(model, stubPrefs);
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
        assertEquals("test1=hidden1&test2=hidden2", portletConfig.getHiddenVariables());
        assertEquals("html1=apa\nhtml2=bepa", portletConfig.getHtmlAttributes());

        assertEquals("apa", portletConfig.getHtmlAttribute("html1", ""));
        assertEquals("tomt", portletConfig.getHtmlAttribute("notThere", "tomt"));

    }

    @Test
    public void testSavePreferencesSecure() throws ValidatorException, IOException, ReadOnlyException {
        PortletConfig portletConfig = PortletConfig.getInstance(stubPrefs);

        MockActionRequest actionRequest = new MockActionRequest();
        actionRequest.setSecure(true);

        CSEditController controller = new CSEditController();
        controller.savePreferences(actionRequest, stubPrefs, portletConfig);

        assertEquals(1, stubPrefs.getStoreCalled());
        assertEquals("https://test-src", portletConfig.getSrc());

        actionRequest.setSecure(false);
        controller.savePreferences(actionRequest, stubPrefs, portletConfig);
        assertEquals("https://test-src", portletConfig.getSrc());
    }

    @Test
    public void testSavePreferencesNotSecure() throws ValidatorException, IOException, ReadOnlyException {
        PortletConfig portletConfig = PortletConfig.getInstance(stubPrefs);

        MockActionRequest actionRequest = new MockActionRequest();
        actionRequest.setSecure(false);

        CSEditController controller = new CSEditController();
        controller.savePreferences(actionRequest, stubPrefs, portletConfig);

        assertEquals(1, stubPrefs.getStoreCalled());
        assertEquals("http://test-src", portletConfig.getSrc());

        actionRequest.setSecure(true);
        controller.savePreferences(actionRequest, stubPrefs, portletConfig);
        assertEquals("http://test-src", portletConfig.getSrc());
    }

    @Test
    public void testSavePreferencesRelative() throws ValidatorException, IOException, ReadOnlyException {
        PortletConfig portletConfig = PortletConfig.getInstance(stubPrefs);
        portletConfig.setSrc("/relative");

        MockActionRequest actionRequest = new MockActionRequest();
        actionRequest.setSecure(false);

        CSEditController controller = new CSEditController();
        controller.savePreferences(actionRequest, stubPrefs, portletConfig);

        assertEquals(1, stubPrefs.getStoreCalled());
        assertEquals("/relative", portletConfig.getSrc());

        actionRequest.setSecure(true);

        controller.savePreferences(actionRequest, stubPrefs, portletConfig);

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
