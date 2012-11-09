package se.vgregion.portal.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.service.CredentialService;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static javax.portlet.PortletRequest.P3PUserInfos.USER_LOGIN_ID;
import static javax.portlet.PortletRequest.USER_INFO;

/**
 * Controller class for the admin view.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Controller
@RequestMapping("VIEW")
public class ViewController {
    private static final Logger LOG = LoggerFactory.getLogger(ViewController.class);

    @Autowired
    private CredentialService credentialService;

    /**
     * Shows the main view populated by {@link UserSiteCredential}s.
     *
     * @param req RenderRequest
     * @param model Model
     * @return the view
     */
    @RenderMapping
    public String showView(RenderRequest req, Model model) {
        String uid = lookupUid(req);

        List<CredentialSiteKeyFormBean> credentials = prepareCredentials(uid);

        model.addAttribute("userCredentials", credentials);

        return "view";
    }

    /**
     * Updates the given {@link UserSiteCredential} in the underlying storage.
     *
     * @param userCredential UserSiteCredential
     * @param model Model
     */
    @ActionMapping("updateCredential")
    public void updateCredential(@ModelAttribute UserSiteCredential userCredential, Model model) {
        try {
            credentialService.save(userCredential);

            model.addAttribute("saveAction", userCredential.getSiteKey());
        } catch (Exception ex) {
            model.addAttribute("saveActionFailed", userCredential.getSiteKey());
        }
    }

    /**
     * Doesn't do anything.
     */
    @ActionMapping("refreshCredential")
    public void refreshCredential() {
    }

    /**
     * Deletes a {@link UserSiteCredential} with a given id.
     *
     * @param userCredentialId the id
     * @param model Model
     */
    @ActionMapping("removeCredential")
    public void deleteCredentials(@RequestParam("userCredentialId") Long userCredentialId, Model model) {
        UserSiteCredential siteCredential = null;
        try {
            siteCredential = credentialService.getUserSiteCredential(userCredentialId);

            credentialService.removeUserSiteCredential(userCredentialId);

            model.addAttribute("removeAction", siteCredential.getSiteKey());
        } catch (RuntimeException ex) {
            if (siteCredential != null) {
                model.addAttribute("removeActionFailed", siteCredential.getSiteKey());
            } else {
                model.addAttribute("removeActionFailed", true);
            }
        }
    }

    private List<CredentialSiteKeyFormBean> prepareCredentials(String uid) {
        List<UserSiteCredential> userCredentials =
                new UserSiteCredentialHelper(credentialService.getAllSiteCredentials(uid)).get();

        List<SiteKey> siteKeys = new SiteKeyHelper(credentialService.getAllSiteKeys())
                .filterActive().get();

        List<CredentialSiteKeyFormBean> credentials = new ArrayList<CredentialSiteKeyFormBean>();
        for (UserSiteCredential credential : userCredentials) {
            for (SiteKey siteKey : siteKeys) {
                if (credential.getSiteKey().equals(siteKey.getSiteKey())) {
                    credentials.add(new CredentialSiteKeyFormBean(credential, siteKey));
                    break;
                }
            }
        }
        // Sort credentials by SiteKey title
        Collections.sort(credentials, new CredentialSiteKeyFormBeanComparator());

        return credentials;
    }

    private static class CredentialSiteKeyFormBeanComparator implements Comparator<CredentialSiteKeyFormBean>,
            Serializable {
        private static final long serialVersionUID = 5880281199565660993L;

        @Override
        public int compare(CredentialSiteKeyFormBean one, CredentialSiteKeyFormBean other) {
            return one.getSiteKey().getTitle().toLowerCase().compareTo(other.getSiteKey().getTitle().toLowerCase());
        }
    }

    private String lookupUid(PortletRequest req) {
        Map<String, ?> userInfo = (Map<String, ?>) req.getAttribute(USER_INFO);
        String userId = "";
        if (userInfo != null && userInfo.get(USER_LOGIN_ID.toString()) != null) {
            userId = (String) userInfo.get(USER_LOGIN_ID.toString());
        }
        return userId;
    }
}
