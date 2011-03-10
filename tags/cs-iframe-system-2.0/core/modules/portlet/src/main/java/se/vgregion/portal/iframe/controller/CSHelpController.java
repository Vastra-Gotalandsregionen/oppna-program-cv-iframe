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

import javax.portlet.PortletPreferences;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.iframe.model.PortletConfig;

/**
 * @author Anders Asplund
 * 
 */
@Controller
@RequestMapping("HELP")
public class CSHelpController {
    @RenderMapping
    public String showHelp(ModelMap model, PortletPreferences prefs) {
        PortletConfig portletConfig = se.vgregion.portal.iframe.model.PortletConfig.getInstance(prefs);
        model.addAttribute("portletConfig", portletConfig);
        return "help";
    }
}
