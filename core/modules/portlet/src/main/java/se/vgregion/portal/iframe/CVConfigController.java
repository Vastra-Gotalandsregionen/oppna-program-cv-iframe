package se.vgregion.portal.iframe;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.ui.ModelMap;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.portlet.*;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.servlet.SessionMessages;

import java.util.Map;
import java.io.IOException;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
//@Controller
//@RequestMapping("EDIT")
public class CVConfigController {
    Logger log = LoggerFactory.getLogger(CVConfigController.class);

    @ActionMapping
    public void processAction(PortletPreferences prefs, ActionRequest actionRequest) throws ReadOnlyException, ValidatorException, IOException {
        log.debug("processAction");

        String cmd = ParamUtil.getString(actionRequest, Constants.CMD);
        log.debug("cmd: "+cmd);
        if (!cmd.equals(Constants.UPDATE)) {
            return;
        }

        String src = ParamUtil.getString(actionRequest, "src");
        log.debug("src: "+src);
        if (!src.startsWith("/") &&
                !StringUtil.startsWith(src, "http://") &&
                !StringUtil.startsWith(src, "https://") &&
                !StringUtil.startsWith(src, "mhtml://")) {

            src = HttpUtil.getProtocol(actionRequest) + "://" + src;
        }

        boolean relative = ParamUtil.getBoolean(actionRequest, "relative");

        String vaultKey = ParamUtil.getString(actionRequest, "vaultKey");
        boolean auth = ParamUtil.getBoolean(actionRequest, "auth");
        String authType = ParamUtil.getString(actionRequest, "authType");
        String formMethod = ParamUtil.getString(actionRequest, "formMethod");
        String userName = ParamUtil.getString(actionRequest, "userName");
        String userNameField = ParamUtil.getString(actionRequest, "userNameField");
        String password = ParamUtil.getString(actionRequest, "password");
        String passwordField = ParamUtil.getString(actionRequest, "passwordField");
        String hiddenVariables = ParamUtil.getString(actionRequest, "hiddenVariables");

        prefs.setValue("src", src);
        prefs.setValue("relative", String.valueOf(relative));
        prefs.setValue("vaultKey", vaultKey);
        prefs.setValue("auth", String.valueOf(auth));
        prefs.setValue("auth-type", authType);
        prefs.setValue("form-method", formMethod);
        prefs.setValue("user-name", userName);
        prefs.setValue("user-name-field", userNameField);
        prefs.setValue("password", password);
        prefs.setValue("password-field", passwordField);
        prefs.setValue("hidden-variables", hiddenVariables);

        String[] htmlAttributes = StringUtil.split(ParamUtil.getString(actionRequest, "htmlAttributes"), "\n");
        log.debug("htmlAttributes: "+htmlAttributes.toString());

        for (int i = 0; i < htmlAttributes.length; i++) {
            log.debug(htmlAttributes[i]);
            int pos = htmlAttributes[i].indexOf("=");

            if (pos != -1) {
                String key = htmlAttributes[i].substring(0, pos);
                String value = htmlAttributes[i].substring(
                        pos + 1, htmlAttributes[i].length());

                prefs.setValue(key, value);
            }
        }

        for (Map.Entry<String,String[]> param : actionRequest.getParameterMap().entrySet()) {
            log.debug(param.getKey()+": ["+StringUtil.merge(param.getValue())+"]");
        }

        prefs.store();

        SessionMessages.add(actionRequest, ".doConfigure");
    }

    @RenderMapping
    public String edit(ModelMap model, RenderRequest req) {
        String currentUser = getRemoteUserId(req);
        log.debug("currentUser: "+ currentUser);

        return "configuration";
    }

    private String getRemoteUserId(PortletRequest request) {
        Map<String, ?> userInfo = (Map<String, ?>) request.getAttribute(PortletRequest.USER_INFO);

        for (Map.Entry entry : userInfo.entrySet()) {
            log.debug("user: "+entry.getKey()+": "+entry.getValue().toString());
        }

        return (userInfo != null) ? (String)userInfo.get(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString()) : "";
    }
}
