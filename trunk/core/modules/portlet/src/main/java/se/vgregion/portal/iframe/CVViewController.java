package se.vgregion.portal.iframe;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.ui.ModelMap;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.portlet.*;

import com.liferay.portal.util.WebKeys;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.SystemException;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.GetterUtil;
import se.vgregion.portal.iframe.model.Credential;

import java.util.Map;
import java.io.IOException;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Controller
@RequestMapping("VIEW")
public class CVViewController {
    Logger log = LoggerFactory.getLogger(CVViewController.class);

    @RenderMapping
    public String showView(PortletPreferences prefs, RenderRequest req, RenderResponse resp, ModelMap model) throws SystemException {
        Credential credential = Credential.getInstance(prefs);
        log.debug("Creds: {}", credential);
        if (credential != null && credential.isAuth() && credential.getSiteUser().length() < 1) {
            model.addAttribute("credential", credential);
            return "credentialForm";
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
    public void storeUserCredential(PortletPreferences prefs, @ModelAttribute("credential") Credential credential) throws ReadOnlyException, ValidatorException, IOException {
        log.debug("storeUserCredential: {}", credential);
        
        prefs.setValue("user-name", credential.getSiteUser());
        prefs.setValue("password", credential.getSitePassword());
        prefs.store();
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
