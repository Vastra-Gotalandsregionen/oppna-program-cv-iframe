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

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
package se.vgregion.portal.iframe.controller;

import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.iframe.model.PortletConfig;
import se.vgregion.portal.iframe.util.LoginScreenScraper;

/**
 * Portlet mode=EDIT controller.
 */
@Controller
@RequestMapping("EDIT")
public class CSEditController {
    private static Logger log = LoggerFactory.getLogger(CSEditController.class);

    @Autowired
    private LoginScreenScraper loginScreenScraper;

    public void setLoginScreenScraper(LoginScreenScraper loginScreenScraper) {
        this.loginScreenScraper = loginScreenScraper;
    }

    /**
     * RenderMapping for edit page.
     * 
     * @param model
     *            - model
     * @param prefs
     *            - portletPreferences
     * @return view
     */
    @RenderMapping
    public String editPreferences(ModelMap model, PortletPreferences prefs) {
        PortletConfig portletConfig = PortletConfig.getInstance(prefs);
        log.debug("editPreferences: {}", portletConfig);

        model.addAttribute("portletConfig", portletConfig);

        loginScreenScraper.advancedScraping(portletConfig);

        return "edit";
    }

    /**
     * 
     * @param actionRequest
     *            - action request
     * @param prefs
     *            - portlet preferences
     * @param portletConfig
     *            - request parameter
     */
    @ActionMapping
    public void savePreferences(ActionRequest actionRequest, PortletPreferences prefs,
            @ModelAttribute("credential") PortletConfig portletConfig) {

        log.debug("savePreferences 1: {}", portletConfig);

        String src = portletConfig.getSrc();
        if (!src.startsWith("/") && !src.startsWith("http://") && !src.startsWith("https://")
                && !src.startsWith("mhtml://")) {

            if (actionRequest.isSecure()) {
                src = "https://" + src;
            } else {
                src = "http://" + src;
            }
            portletConfig.setSrc(src);
        }

        log.debug("savePreferences 2: {}", portletConfig);

        try {
            portletConfig.store(prefs);
        } catch (ValidatorException e) {
            e.printStackTrace();
        }

        log.debug("src: {}", prefs.getValue("src", ""));
    }
}
