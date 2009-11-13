package se.vgregion.portal.iframe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.WindowState;

import se.vgregion.portal.iframe.model.PortletConfig;
import se.vgregion.portal.iframe.model.UserSiteCredential;
import se.vgregion.portal.repository.CredentialVaultRepository;

import java.util.Map;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Controller
@RequestMapping("VIEW")
public class CVViewController {
    private Logger log = LoggerFactory.getLogger(CVViewController.class);

    private CredentialVaultRepository credentialVaultRepository;

    @Autowired
    public void setCredentialVaultRepository(CredentialVaultRepository credentialVaultRepository) {
        this.credentialVaultRepository = credentialVaultRepository;
    }

    /**
     * Main controllermethod.
     * Handling of user-sitecredential availability and iFrame source linking
     *
     * @param prefs - protlet preferences
     * @param req  - request
     * @param resp - response
     * @param model - model
     * @return view
     */
    @RenderMapping
    public String showView(PortletPreferences prefs, RenderRequest req, RenderResponse resp, ModelMap model) {
        PortletConfig portletConfig = PortletConfig.getInstance(prefs);
        model.addAttribute("portletConfig", portletConfig);
        log.debug("Creds: {}", portletConfig);

        UserSiteCredential siteCredential = new UserSiteCredential();
        if (!credentialsAvailable(req, model, portletConfig, siteCredential)) {
            return "userCredentialForm";
        }

        String iFrameSrc = prepareView(resp, portletConfig, siteCredential);
        String baseSrc = getBaseSrc(iFrameSrc);

        String iFrameHeight = getIFrameHeight(req, portletConfig);

        model.addAttribute("iFrameSrc", iFrameSrc);
        model.addAttribute("baseSrc", baseSrc);
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

        return "view";
    }

    /**
     * Credential view handleing.
     *
     * @param prefs - portlet preferences
     * @param req - request
     * @param model - model
     * @return view
     */
    @RenderMapping(params = "action=changeVaultCredentials")
    public String changeVaultCredentials(PortletPreferences prefs, RenderRequest req, ModelMap model) {
        PortletConfig portletConfig = PortletConfig.getInstance(prefs);
        model.addAttribute("portletConfig", portletConfig);
        if (!portletConfig.isAuth()) {
            return "view";
        }

        UserSiteCredential siteCredential = new UserSiteCredential();
        credentialsAvailable(req, model, portletConfig, siteCredential);

        return "userCredentialForm";
    }

    /**
     * Prepare proxyLoginForm.jsp for form-based authentication.
     *
     * @param model - model
     * @param req - request
     * @param prefs - portlet preferences
     * @return view
     */
    @ResourceMapping
    public String showProxyForm(PortletPreferences prefs, ResourceRequest req, ModelMap model) {
        final PortletConfig portletConfig = se.vgregion.portal.iframe.model.PortletConfig.getInstance(prefs);
        model.addAttribute("portletConfig", portletConfig);
        if (!portletConfig.isAuth() || !"form".equals(portletConfig.getAuthType())) {
            return "view";
        }

        UserSiteCredential siteCredential = new UserSiteCredential();
        credentialsAvailable(req, model, portletConfig, siteCredential);

        return "proxyLoginForm";
    }

    /**
     * Store User-SiteCredentials in the Credential-Vault.
     * Action posted from userCredentailForm.jsp
     *
     * @param siteCredential - credential
     */
    @ActionMapping
    public void storeUserCredential(@ModelAttribute("siteCredential") UserSiteCredential siteCredential) {
        credentialVaultRepository.addUserSiteCredential(siteCredential);
        log.debug("storeUserCredential: {}", siteCredential);
    }

    private String prepareView(RenderResponse resp, PortletConfig portletConfig, UserSiteCredential siteCredential) {
        String iFrameSrc = getDefaultTarget();

        String src = portletConfig.getSrc();

        if (portletConfig.isAuth()) {
            if (portletConfig.getAuthType().equals("basic")) {
                // goto src with basic authentication
                int pos = src.indexOf("://");

                String protocol = src.substring(0, pos + 3);
                String url = src.substring(pos + 3, src.length());

                src = protocol + siteCredential.getSiteUser() + ":" + siteCredential.getSitePassword() + "@" + url;
            } else {
                // goto proxy for form login
                src = resp.createResourceURL().toString();
            }
        }

        if (src.length() > 0) {
            iFrameSrc = src;
        }

        return iFrameSrc;
    }

    private String getDefaultTarget() {
        return "http://vgregion.se";
    }

    private boolean credentialsAvailable(PortletRequest req, ModelMap model, PortletConfig portletConfig, UserSiteCredential returnSiteCredential) {
        boolean userSiteCredentialExist = true;
        if (portletConfig.isAuth()) {
            String uid = lookupUid(req);
            returnSiteCredential.setUid(uid);
            returnSiteCredential.setSiteKey(portletConfig.getSiteKey());

            UserSiteCredential siteCredential = credentialVaultRepository.getUserSiteCredential(uid, portletConfig.getSiteKey());
            if (siteCredential != null) {
                returnSiteCredential.setSiteUser(siteCredential.getSiteUser());
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
            userId = (String) "";
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