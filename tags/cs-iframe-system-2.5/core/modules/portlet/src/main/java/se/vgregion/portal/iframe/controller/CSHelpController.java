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
 * 
 */
package se.vgregion.portal.iframe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.service.CredentialService;
import se.vgregion.portal.iframe.model.PortletConfig;

import javax.portlet.PortletPreferences;

/**
 * Class for the request mappings for the help page.
 *
 * @author Anders Asplund
 */
@Controller
@RequestMapping("HELP")
public class CSHelpController {

    @Autowired
    private CredentialService credentialService;

    /**
     * Show the help page.
     *
     * @param model Model
     * @param prefs PortletPreferences
     * @return help view
     */
    @RenderMapping
    public String showHelp(Model model, PortletPreferences prefs) {
        PortletConfig portletConfig = se.vgregion.portal.iframe.model.PortletConfig.getInstance(prefs);
        model.addAttribute("portletConfig", portletConfig);

        SiteKey siteKey = credentialService.getSiteKey(portletConfig.getSiteKey());
        model.addAttribute("siteKey", siteKey);

        return "help";
    }
}
