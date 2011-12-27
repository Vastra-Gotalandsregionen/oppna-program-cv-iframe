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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.portal.admin.controller.SiteKeyHelper;
import se.vgregion.portal.cs.domain.Form;
import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.service.CredentialService;
import se.vgregion.portal.cs.service.LoginformService;
import se.vgregion.portal.iframe.model.PortletConfig;

import javax.net.ssl.*;
import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.ValidatorException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Portlet mode=EDIT controller.
 */
@Controller
@RequestMapping("EDIT")
public class CSEditController {
    private static Logger log = LoggerFactory.getLogger(CSEditController.class);

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private LoginformService loginformService;

    /**
     * RenderMapping for edit page.
     * 
     * @param model - model
     * @param prefs - portletPreferences
     * @return view
     */
    @RenderMapping
    public String editPreferences(ModelMap model, PortletPreferences prefs) {
        PortletConfig portletConfig = PortletConfig.getInstance(prefs);
        log.debug("editPreferences: {}", portletConfig);
        model.addAttribute("portletConfig", portletConfig);

        List<SiteKey> siteKeys = new SiteKeyHelper(credentialService.getAllSiteKeys()).filterActive()
                .orderBySiteKey().get();
        model.addAttribute("siteKeys", siteKeys);

        return "edit";
    }

    @RenderMapping(params = "action=loginExtractor")
    public String loginExtractor(PortletPreferences prefs, Model model) {
        PortletConfig portletConfig = PortletConfig.getInstance(prefs);
        String loginFormUrl = portletConfig.getSrc();
        model.addAttribute("loginformUrl", loginFormUrl);

        SSLSocketFactory oldSSslSocketFactory = null;
        try {
            oldSSslSocketFactory = trustAllSSLSocketFactory();

            Document doc = Jsoup.parse(new URL(loginFormUrl), 3000);
            model.addAttribute("loginformContent", doc.html());

            List<Form> loginforms = loginformService.extract(doc);
            model.addAttribute("loginforms", loginforms);
        } catch (Exception e) {
            model.addAttribute("loginformContent", "Failed to lookup page content");
            model.addAttribute("error", e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (oldSSslSocketFactory != null)
                resetSSLSocketFactory(oldSSslSocketFactory);
        }

        return "loginExtractor";
    }

    private SSLSocketFactory trustAllSSLSocketFactory() {
        SSLSocketFactory oldSslSocketFactory = null;
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            oldSslSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
        return oldSslSocketFactory;
    }

    private void resetSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
    }

    /**
     * Save an instance of {@link PortletPreferences}.
     *
     * @param actionRequest - action request
     * @param prefs - portlet preferences
     * @param portletConfig - request parameter
     */
    @ActionMapping
    public void savePreferences(ActionRequest actionRequest, PortletPreferences prefs,
            @ModelAttribute("credential") PortletConfig portletConfig) {

        log.debug("savePreferences 1: {}", portletConfig);

        // Link should be on format http(s):/the.domain.com/
        fixSrcLinkOnPortletConfig(portletConfig, actionRequest.isSecure());

        log.debug("savePreferences 2: {}", portletConfig);

        try {
            portletConfig.store(prefs);
        } catch (ValidatorException e) {
            e.printStackTrace();
        }

        log.debug("src: {}", prefs.getValue("src", ""));
    }

    private void fixSrcLinkOnPortletConfig(PortletConfig portletConfig, boolean secureAction) {
        String src = portletConfig.getSrc();

        // Ensure link starts with http or https
        if (!src.startsWith("/") && !src.startsWith("http://") && !src.startsWith("https://")
                && !src.startsWith("mhtml://")) {
            if (secureAction) {
                src = "https://" + src;
            } else {
                src = "http://" + src;
            }
            portletConfig.setSrc(src);
        }
    }
}
