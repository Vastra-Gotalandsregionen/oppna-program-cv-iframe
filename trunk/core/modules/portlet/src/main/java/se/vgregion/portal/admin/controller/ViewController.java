package se.vgregion.portal.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.service.CredentialService;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;

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
    private CredentialService userSiteCredentialService;

    @RenderMapping
    public String showView(RenderRequest req, ModelMap model) {
        String uid = lookupUid(req);

        Collection<UserSiteCredential> userCredentials = userSiteCredentialService.getAllSiteCredentials(uid);
        model.addAttribute("userCredentials", userCredentials);

        return "view";
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
