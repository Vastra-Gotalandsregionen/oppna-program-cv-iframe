package se.vgregion.portal.iframe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.portlet.*;

import se.vgregion.portal.iframe.model.Credential;
import se.vgregion.portal.iframe.model.UserSiteCredential;
import se.vgregion.portal.repository.CredentialVaultRepository;

import java.util.Map;
import java.io.IOException;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("siteCredential")
public class CVViewController {
    private Logger log = LoggerFactory.getLogger(CVViewController.class);

    private CredentialVaultRepository credentialVaultRepository = null;

    @Autowired
    public void setCredentialVaultRepository(CredentialVaultRepository credentialVaultRepository) {
        this.credentialVaultRepository = credentialVaultRepository;
    }

    /**
     * Main controllermethod.
     * Handling of user-sitecredential availability and iFrame source linking
     *
     * @param prefs
     * @param req
     * @param resp
     * @param model
     * @return
     */
    @RenderMapping
    public String showView(PortletPreferences prefs, RenderRequest req, RenderResponse resp, ModelMap model) {
        Credential credential = Credential.getInstance(prefs);
        model.addAttribute("credential", credential);
        log.debug("Creds: {}", credential);

        UserSiteCredential siteCredential = null;
        if (!credentialsAvailable(req, model, credential, siteCredential)) return "userCredentialForm";

        String iFrameSrc = prepareView(resp, credential, siteCredential);
        String baseSrc = getBaseSrc(iFrameSrc);

        String iFrameHeight = getIFrameHeight(req, credential);

        model.addAttribute("iFrameSrc", iFrameSrc);
        model.addAttribute("baseSrc", baseSrc);
        model.addAttribute("iFrameHeight", iFrameHeight);
        model.addAttribute("border", credential.getHtmlAttribute("border", "0"));
        model.addAttribute("bordercolor", credential.getHtmlAttribute("bordercolor", "#000000"));
        model.addAttribute("frameborder", credential.getHtmlAttribute("frameborder", "0"));
        model.addAttribute("height-maximized", credential.getHtmlAttribute("height-maximized", "600"));
        model.addAttribute("height-normal", credential.getHtmlAttribute("height-normal", "300"));
        model.addAttribute("hspace", credential.getHtmlAttribute("hspace", "0"));
        model.addAttribute("scrolling", credential.getHtmlAttribute("scrolling", "auto"));
        model.addAttribute("vspace", credential.getHtmlAttribute("vspace", "0"));
        model.addAttribute("width", credential.getHtmlAttribute("width", "100%"));

        return "view";
    }

    @RenderMapping(params = "action=changeVaultCredentials")
    public String changeVaultCredentials(PortletPreferences prefs, RenderRequest req, ModelMap model) {
        Credential credential = Credential.getInstance(prefs);
        model.addAttribute("credential", credential);
        UserSiteCredential siteCredential = null;
        credentialsAvailable(req, model, credential, siteCredential);

        return "userCredentialForm";
    }

    /**
     * Prepare proxyLoginForm.jsp for form-based authentication
     *
     * @param model
     * @param prefs
     * @return
     */
    @ResourceMapping
    public String showProxyForm(ModelMap model, PortletPreferences prefs) {
        final Credential credential = Credential.getInstance(prefs);
        model.addAttribute("credential", credential);

        return "proxyLoginForm";
    }

    /**
     * Store User-SiteCredentials in the Credential-Vault.
     * Action posted from userCredentailForm.jsp
     *
     * @param siteCredential
     * @throws ReadOnlyException
     * @throws ValidatorException
     * @throws IOException
     */
    @ActionMapping
    public void storeUserCredential(@ModelAttribute("siteCredential") UserSiteCredential siteCredential) throws ReadOnlyException, ValidatorException, IOException {
        credentialVaultRepository.addUserSiteCredential(siteCredential);
        log.debug("storeUserCredential: {}", siteCredential);
    }

    private String prepareView(RenderResponse resp, Credential credential, UserSiteCredential siteCredential) {
        String iFrameSrc = getDefaultTarget();

        String src = credential.getSrc();

        if (credential.isAuth()) {
            if (credential.getAuthType().equals("basic")) {
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

    private boolean credentialsAvailable(RenderRequest req, ModelMap model, Credential credential, UserSiteCredential siteCredential) {
        if (credential.isAuth()) {
            String uid = lookupUid(req);
            siteCredential = credentialVaultRepository.getUserSiteCredential(uid, credential.getSiteKey());
            model.addAttribute("siteCredential", siteCredential);

            if (siteCredential == null) {
                siteCredential = new UserSiteCredential(uid, credential.getSiteKey());
                model.addAttribute("siteCredential", siteCredential);
                return false;
            }
        }
        return true;
    }

    private String lookupUid(RenderRequest req) {
        Map<String, ?> userInfo = (Map<String, ?>) req.getAttribute(PortletRequest.USER_INFO);
        String userId = (String) ((userInfo != null) ? userInfo.get(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString()) : "");
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

    private String getIFrameHeight(RenderRequest req, Credential credential) {
        WindowState windowState = req.getWindowState();
        String iFrameHeight = (windowState.equals(WindowState.MAXIMIZED)) ? credential.getHtmlAttribute("height-normal", "300") : credential.getHtmlAttribute("height-maximized", "600");
        return iFrameHeight;
    }
}
