package se.vgregion.portal.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.portal.csiframe.service.CredentialService;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import java.security.Principal;
import java.util.Map;

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
    public String showView(RenderRequest req) {
        String uid = lookupUid(req);

        userSiteCredentialService.getUserSiteCredential(uid, "");

        return "view";
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
