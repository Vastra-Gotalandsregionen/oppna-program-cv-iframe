package se.vgregion.portal.iframe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.ui.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.portlet.*;

import com.liferay.portal.util.WebKeys;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.GetterUtil;
import se.vgregion.portal.iframe.model.Credential;
import se.vgregion.portal.iframe.model.UserSiteCredential;
import se.vgregion.portal.iframe.repository.CredentialVaultRepository;

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
    public void setCredentialVaultRepository(final CredentialVaultRepository credentialVaultRepository) {
        this.credentialVaultRepository = credentialVaultRepository;
    }

    @RenderMapping
    public String showView(PortletPreferences prefs, RenderRequest req, RenderResponse resp, ModelMap model) throws SystemException {
        Credential credential = Credential.getInstance(prefs);
        log.debug("Creds: {}", credential);
        if (credential.isAuth()) {
            model.addAttribute("credential", credential);

            String uid = lookupUid(req);
            log.debug("UID: {}, SiteKey: {}", uid, credential.getSiteKey());

            UserSiteCredential siteCredential = credentialVaultRepository.getUserSiteCredential(uid, credential.getSiteKey());
            if (siteCredential == null) {
                siteCredential = new UserSiteCredential(uid, credential.getSiteKey());
                model.addAttribute("siteCredential", siteCredential);
                return "credentialForm";
            }
            model.addAttribute("siteCredential", siteCredential);
        }

        String iFrameSrc = transformSrc(req, resp);

        if (Validator.isNull(iFrameSrc)) {
            iFrameSrc = "http://vgregion.se";
        }

        req.setAttribute(WebKeys.IFRAME_SRC, iFrameSrc);
        log.debug("iFrameSrc: "+iFrameSrc);

        return "view";
    }

    @ResourceMapping
    public String showProxyForm(ModelMap model, PortletPreferences prefs) {
        final Credential credential = Credential.getInstance(prefs);
        log.debug("showProxyForm: {}", credential);
        model.addAttribute("credential", credential);
        
        return "proxy";
    }

    @ActionMapping
    public void storeUserCredential(final @ModelAttribute("siteCredential") UserSiteCredential siteCredential) throws ReadOnlyException, ValidatorException, IOException {
        credentialVaultRepository.addUserSiteCredential(siteCredential);
        log.debug("storeUserCredential: {}", siteCredential);
    }

    private String lookupUid(RenderRequest req) {
        Map<String, ?> userInfo = (Map<String, ?>) req.getAttribute(PortletRequest.USER_INFO);
        String userId = (String) ((userInfo != null) ? userInfo.get(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString()) : "");
        return userId;
    }

    protected String getSrc(RenderRequest renderRequest) {
        PortletPreferences preferences = renderRequest.getPreferences();
        String src = preferences.getValue("src", StringPool.BLANK);
        src = ParamUtil.getString(renderRequest, "src", src);

        return src;
    }

    protected String getUserName(RenderRequest renderRequest) {
        PortletPreferences preferences = renderRequest.getPreferences();
        String userName = preferences.getValue("user-name", StringPool.BLANK);
        if (Validator.isNull(userName)) {
            userName = renderRequest.getRemoteUser();
        }

        return userName;
    }

    protected String getPassword(RenderRequest renderRequest) {
        PortletPreferences preferences = renderRequest.getPreferences();
        String password = preferences.getValue("password", StringPool.BLANK);
        if (Validator.isNull(password)) {
            password = PortalUtil.getUserPassword(renderRequest);
        }

        return password;
    }

    protected String transformSrc(RenderRequest renderRequest, RenderResponse response) throws SystemException {
        PortletPreferences preferences = renderRequest.getPreferences();

        String src = getSrc(renderRequest);

        boolean auth = GetterUtil.getBoolean(preferences.getValue("auth", StringPool.BLANK));
        String authType = preferences.getValue("auth-type", StringPool.BLANK);
        String userName = getUserName(renderRequest);
        String password = getPassword(renderRequest);

        if (auth) {
            if (authType.equals("basic")) {
                int pos = src.indexOf("://");

                String protocol = src.substring(0, pos + 3);
                String url = src.substring(pos + 3, src.length());

                src = protocol + userName + ":" + password + "@" + url;
            } else {

                src = response.createResourceURL().toString();
            }
        }

        return src;
    }
}
