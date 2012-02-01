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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.util.PortletUtils;
import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.service.CredentialService;
import se.vgregion.portal.iframe.model.PortletConfig;

import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.WindowState;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("postLogin")
public class CSViewController {
    private Logger log = LoggerFactory.getLogger(CSViewController.class);

    @Autowired
    private CredentialService credentialService;

    @ModelAttribute("postLogin")
    public String initPostLogin() {
        return "";
    }

    /**
     * Main controllermethod. Handling of user-sitecredential availability and iFrame source linking.
     *
     * @param prefs - portlet preferences
     * @param req   - request
     * @param resp  - response
     * @param model - model
     * @return view
     */
    @RenderMapping
    public String showView(PortletPreferences prefs, RenderRequest req, RenderResponse resp, ModelMap model,
            @ModelAttribute("postLogin") String postLogin) {
        PortletConfig portletConfig = PortletConfig.getInstance(prefs);
        log.debug("Creds: {}", portletConfig);

        // Site Credentials
        SiteKey siteKey = credentialService.getSiteKey(portletConfig.getSiteKey());
        model.addAttribute("siteKey", siteKey);

        UserSiteCredential siteCredential = new UserSiteCredential();
        if (siteKey != null && !credentialsAvailable(req, model, portletConfig, siteCredential, siteKey)) {
            model.addAttribute("portletConfig", portletConfig);
            return "userCredentialForm";
        }
        // ------------------------------------

        // Resolve postLogin from friendly-url
        String newPostLogin = req.getParameter("postLogin");
        while (StringUtils.isNotBlank(newPostLogin) && newPostLogin.endsWith("null")) {
            newPostLogin = newPostLogin.substring(0, newPostLogin.length() - 4);
        }
        if (StringUtils.isNotBlank(newPostLogin)) {
            try {
                postLogin = new String(Base64.decodeBase64(newPostLogin));
                if ("basic".equals(portletConfig.getAuthType())) {
                    postLogin = prepareBasicAuthAction(siteCredential, postLogin);
                }
                model.addAttribute("postLogin", postLogin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // ------------------------------------

        // PreLogin action, iFrame url (url to virtual form, or an basic auth url to site)
        String iFrameSrc = prepareView(resp, req, portletConfig, siteCredential);
        String preIFrameSrc;
        if (StringUtils.isNotBlank(portletConfig.getPreIFrameAction())) {
            preIFrameSrc = portletConfig.getPreIFrameAction();
        } else {
            preIFrameSrc = iFrameSrc;
        }

        String baseSrc = getBaseSrc(iFrameSrc);


        model.addAttribute("iFrameSrc", iFrameSrc);
        model.addAttribute("preIFrameSrc", preIFrameSrc);
        model.addAttribute("baseSrc", baseSrc);
        // ------------------------------------

        // iFrame display configuration
        String iFrameHeight = getIFrameHeight(req, portletConfig);
        model.addAttribute("iFrameHeight", iFrameHeight);

        model.addAttribute("border", portletConfig.getHtmlAttribute("border", "0"));
        model.addAttribute("bordercolor", portletConfig.getHtmlAttribute("bordercolor", "#000000"));
        model.addAttribute("frameborder", portletConfig.getHtmlAttribute("frameborder", "0"));
        model.addAttribute("height-maximized", portletConfig.getHtmlAttribute("height-maximized", "600"));
        model.addAttribute("height-normal", portletConfig.getHtmlAttribute("height-normal", "300"));
        model.addAttribute("hspace", portletConfig.getHtmlAttribute("hspace", "0"));
        model.addAttribute("scrolling", portletConfig.getHtmlAttribute("scrolling", "auto"));
        model.addAttribute("vspace", portletConfig.getHtmlAttribute("vspace", "0"));
        model.addAttribute("width", portletConfig.getHtmlAttribute("width", "100%"));

        String linkDisplay = portletConfig.isAuth() ? "display:block;" : "display:none;";
        model.addAttribute("link_display", linkDisplay);
        model.addAttribute("myPortletConfig", portletConfig);
        // ------------------------------------

        return "view";
    }

    /**
     * Credential view handleing.
     *
     * @param prefs - portlet preferences
     * @param req   - request
     * @param model - model
     * @return userCredentialForm
     */
    @RenderMapping(params = "action=changeVaultCredentials")
    public String changeVaultCredentials(PortletPreferences prefs, RenderRequest req, ModelMap model) {
        PortletConfig portletConfig = PortletConfig.getInstance(prefs);
        model.addAttribute("portletConfig", portletConfig);
        if (!portletConfig.isAuth()) {
            return "view";
        }

        SiteKey siteKey = credentialService.getSiteKey(portletConfig.getSiteKey());
        model.addAttribute("siteKey", siteKey);

        UserSiteCredential siteCredential = new UserSiteCredential();
        credentialsAvailable(req, model, portletConfig, siteCredential, siteKey);

        return "userCredentialForm";
    }

    /**
     * Prepare proxyLoginForm.jsp for form-based authentication.
     *
     * @param model - model
     * @param req   - request
     * @param prefs - portlet preferences
     * @return proxyLoginForm
     * @throws URISyntaxException
     */
    @ResourceMapping
    public String showProxyForm(PortletPreferences prefs, ResourceRequest req, ModelMap model,
            @ModelAttribute("postLogin") String postLogin) {

        PortletConfig portletConfig = PortletConfig.getInstance(prefs);
        model.addAttribute("portletConfig", portletConfig);
        if (!portletConfig.isAuth()) {
            return "view";
        }

        // 1: Credentials
        SiteKey siteKey = credentialService.getSiteKey(portletConfig.getSiteKey());
        model.addAttribute("siteKey", siteKey);

        UserSiteCredential siteCredential = new UserSiteCredential();
        credentialsAvailable(req, model, portletConfig, siteCredential, siteKey);

        // 2: postLogin
        if (postLogin != null && postLogin.length() > 0) {
            model.addAttribute("postLoginLink", true);
        }

        // 3: Dynamic Field
        if (StringUtils.isNotBlank(portletConfig.getDynamicField())) {
            model.addAttribute("hasDynamicField", true);
            String dynamicValue = lookupDynamicValue(portletConfig);
            model.addAttribute("dynamicValue", dynamicValue);
        }

        // 4: RD encode
        if (portletConfig.isRdEncode()) {
            model.addAttribute("rdPass", encodeRaindancePassword(lookupUid(req), portletConfig));
        }

        URI src = null;
        try {
            src = new URI(portletConfig.getSrc());

            String proxyAction;
            if (portletConfig.getFormAction() == null || portletConfig.getFormAction().length() < 1) {
                proxyAction = src.toString();
            } else {
                proxyAction = src.resolve(portletConfig.getFormAction()).toString();
            }

            if ("basic".equals(portletConfig.getAuthType())) {
                proxyAction = prepareBasicAuthAction(siteCredential, proxyAction);
            }

            model.addAttribute("proxyAction", proxyAction);
            debug(req, proxyAction);

            return "proxyLoginForm";
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
             return "help";
        }

    }

    private String prepareBasicAuthAction(UserSiteCredential siteCredential, String action) {
        String[] splitUrl = action.split("://");
        if (splitUrl.length != 2) {
            return action; // Cannot be processed
        } else {
            String protocol = splitUrl[0];
            String url = splitUrl[1];

            action = String.format("%s://%s:%s@%s", protocol, siteCredential.getSiteUser(),
                    siteCredential.getSitePassword(), url);
        }
        return action;
    }

    private String lookupDynamicValue(PortletConfig portletConfig) {
        try {
            Document doc = new JSoupHelper().invoke(new URL(portletConfig.getDynamicFieldAction()), 1500);
            String dynamicValue = doc.select("body").get(0).text().trim()
                    .replaceAll("\n\r", "")
                    .replaceAll("\r", "")
                    .replaceAll("\n", "");
            if (dynamicValue.contains("<>")) throw new Exception("Invalid value format ["+dynamicValue+"]");
            return dynamicValue;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    private String encodeRaindancePassword(String uid, PortletConfig portletConfig) {
        try {
            Document doc = new JSoupHelper().invoke(new URL(portletConfig.getSrc()), 1500);

            Element dynamicValue = doc.getElementById("loginForm:_idJsp10");
            String onClick = dynamicValue.attr("onclick");
            String sessionKey = onClick.split("'")[3];
            UserSiteCredential siteCredential = credentialService.getUserSiteCredential(uid,
                    portletConfig.getSiteKey());

            return encodeRaindance(siteCredential.getSitePassword(), sessionKey);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String encodeRaindance(String sitePassword, String sessionKey) {
        String tot = (sitePassword.length() > 9) ? ""+sitePassword.length() : "0" + sitePassword.length();
        String workStr = sessionKey.substring(0, 5) + tot + sitePassword + sessionKey.substring(5);
        
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<workStr.length(); i++) {
            int tmp = workStr.charAt(i);
            if (tmp % 2 > 0) {
                tmp = (tmp * 3) + 42;
            } else {
                tmp = (tmp * 2) + 20;
            }
            
            String pre = (tmp < 10) ? "00" : (tmp < 100) ? "0" : "";
            
            sb.append(pre).append(tmp);
        }
        
        return sb.toString();
    }

    private void debug(ResourceRequest req, String proxyFormAction) {
        if (log.isDebugEnabled()) {
            log.debug("ProxyFormAction: {}", proxyFormAction);

            log.debug("Request attributes");
            Enumeration attrs = req.getAttributeNames();
            while (attrs.hasMoreElements()) {
                String attr = attrs.nextElement().toString();
                log.debug("Request attribute: {} : {}", attr, req.getAttribute(attr));
            }

            log.debug("Request parameters");
            Enumeration params = req.getParameterNames();
            while (params.hasMoreElements()) {
                String param = params.nextElement().toString();
                log.debug("Request parameters: {} : {}", param, req.getParameterValues(param));
            }
        }
    }

    /**
     * Store User-SiteCredentials in the Credential-Vault. Action posted from userCredentailForm.jsp
     *
     * @param siteCredential - credential
     * @param req            - action request to handle cancel
     */
    @Transactional
    @ActionMapping
    public void storeUserCredential(@ModelAttribute("siteCredential") UserSiteCredential siteCredential,
            ActionRequest req) {

        if (StringUtils.isBlank(siteCredential.getUid())) {
            throw new RuntimeException("ERROR: Unknown user. Cannot store credential.");
        }

        if (StringUtils.isBlank(siteCredential.getSiteKey())) {
            throw new RuntimeException("CONFIGURATION ERROR: No site-key given. Cannot store user credential.");
        }
        if (!PortletUtils.hasSubmitParameter(req, "_cancel")) {
            UserSiteCredential entToStore = credentialService.getUserSiteCredential(
                    siteCredential.getUid(), siteCredential.getSiteKey());
            if (entToStore == null || entToStore.getId() == null) {
                entToStore = siteCredential;
            } else {
                entToStore.setSiteUser(siteCredential.getSiteUser());
                entToStore.setSitePassword(siteCredential.getSitePassword());
            }
            // credentialStoreRepository.save(siteCredential);
            credentialService.save(entToStore);
            // log.debug("storeUserCredential: {}", siteCredential);
        }
    }

    private String prepareView(RenderResponse resp, RenderRequest req, PortletConfig portletConfig, UserSiteCredential siteCredential) {
        String iFrameSrc = getDefaultTarget();

        String src = portletConfig.getSrc();

        if (portletConfig.isAuth()) {
//            if (portletConfig.getAuthType().equals("basic")) {
//                String[] splitUrl = src.split("://");
//                if (splitUrl.length != 2) {
//                    src = ""; // Cannot be processed
//                } else {
//                    String protocol = splitUrl[0];
//                    String url = splitUrl[1];
//
//                    src = String.format("%s://%s:%s@%s", protocol, siteCredential.getSiteUser(),
//                            siteCredential.getSitePassword(), url);
//                }
//            } else {
                // goto proxy for form login
                src = resp.createResourceURL().toString();
//            }
        }

        if (src.length() > 0) {
            iFrameSrc = src;
        }

        if (portletConfig.isUserLoggedIn()) {
            if (iFrameSrc.indexOf("?") == -1) {
                iFrameSrc += "?userId=" + lookupUid(req);
            } else {
                iFrameSrc += "&userId=" + lookupUid(req);
            }
        }

        return iFrameSrc;
    }

    private String getDefaultTarget() {
        return "http://vgregion.se/";
    }

    private boolean credentialsAvailable(PortletRequest req, ModelMap model, PortletConfig portletConfig,
            UserSiteCredential returnSiteCredential, SiteKey siteKey) {
        boolean userSiteCredentialExist = true;
        if (portletConfig.isAuth()) {
            String uid = lookupUid(req);
            returnSiteCredential.setUid(uid);
            returnSiteCredential.setSiteKey(portletConfig.getSiteKey());
            if (siteKey.getScreenNameOnly() || siteKey.getSuggestScreenName()) {
                returnSiteCredential.setSiteUser(uid);
            }

            UserSiteCredential siteCredential = credentialService.getUserSiteCredential(uid,
                    portletConfig.getSiteKey());

            if (siteCredential != null) {
                returnSiteCredential.setId(siteCredential.getId());
                if (!siteKey.getScreenNameOnly()) {
                    returnSiteCredential.setSiteUser(siteCredential.getSiteUser());
                }
                returnSiteCredential.setSitePassword(siteCredential.getSitePassword());
            } else {
                userSiteCredentialExist = false;
            }
            model.addAttribute("siteCredential", returnSiteCredential);
        }
        return userSiteCredentialExist;
    }

    private String lookupUid(PortletRequest req) {
        Map<String, ?> userInfo = (Map<String, ?>) req.getAttribute(PortletRequest.USER_INFO);
        String userId;
        if (userInfo != null) {
            userId = (String) userInfo.get(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString());
        } else {
            userId = "";
        }
        return userId;
    }

    private String getBaseSrc(String iFrameSrc) {
        String baseSrc = iFrameSrc;
        int lastSlashPos = iFrameSrc.substring(7).lastIndexOf("/");
        if (lastSlashPos != -1) {
            baseSrc = iFrameSrc.substring(0, lastSlashPos + 8);
        }
        return baseSrc;
    }

    private String getIFrameHeight(RenderRequest req, PortletConfig portletConfig) {
        WindowState windowState = req.getWindowState();
        String iFrameHeight;
        if (windowState.equals(WindowState.NORMAL)) {
            iFrameHeight = portletConfig.getHtmlAttribute("height-normal", "300");
        } else {
            iFrameHeight = portletConfig.getHtmlAttribute("height-maximized", "600");
        }
        return iFrameHeight;
    }
}
