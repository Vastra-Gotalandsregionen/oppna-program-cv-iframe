package se.vgregion.portal.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.service.CredentialService;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import java.security.Principal;
import java.util.*;

import static javax.portlet.PortletRequest.P3PUserInfos.USER_LOGIN_ID;
import static javax.portlet.PortletRequest.USER_INFO;

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Controller
@RequestMapping("VIEW")
public class ViewController {
    private static final Logger LOG = LoggerFactory.getLogger(ViewController.class);

    @Autowired
    private CredentialService credentialService;

    @RenderMapping
    public String showView(RenderRequest req, ModelMap model) {
        String uid = lookupUid(req);

        List<UserSiteCredential> userCredentials =
                new UserSiteCredentialHelper(new ArrayList(credentialService.getAllSiteCredentials(uid))).
                        orderBySiteKey().get();

        List<SiteKey> siteKeys = new SiteKeyHelper(new ArrayList(credentialService.getAllSiteKeys())).
                orderBySiteKey().filterActive().get();

        List<CredentialSiteKey> credentials = new ArrayList<CredentialSiteKey>();
        for (UserSiteCredential credential : userCredentials) {
            for (SiteKey siteKey: siteKeys) {
                if (credential.getSiteKey().equals(siteKey.getSiteKey())) {
                    credentials.add(new CredentialSiteKey(credential, siteKey));
                }
            }
        }

        model.addAttribute("userCredentials", credentials);

        return "view";
    }

    @ActionMapping("updateCredential")
    public void updateCredential(@ModelAttribute UserSiteCredential userCredential, ActionRequest actionRequest) {
        credentialService.save(userCredential);
    }

    @ActionMapping("refreshCredential")
    public void refreshCredential() {
    }

    private String lookupUid(PortletRequest req) {
        Map<String, ?> userInfo = (Map<String, ?>) req.getAttribute(USER_INFO);
        String userId = "";
        if (userInfo != null && userInfo.get(USER_LOGIN_ID.toString()) != null) {
            userId = (String) userInfo.get(USER_LOGIN_ID.toString());
        }
        return userId;
    }

    private Long getCurrentUserId(PortletRequest request) throws PortletSecurityException {
        final Principal userPrincipal = request.getUserPrincipal();
        try {
            String userIdStr = userPrincipal.getName();
            return Long.parseLong(userIdStr);
        } catch (Exception e) {
            LOG.warn("No user session exists.");
            throw new PortletSecurityException("No user session exists.", e);
        }
    }
}
