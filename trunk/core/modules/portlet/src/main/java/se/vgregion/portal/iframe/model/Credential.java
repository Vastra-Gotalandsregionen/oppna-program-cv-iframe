package se.vgregion.portal.iframe.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.core.style.ToStringStyler;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import java.io.IOException;
import java.io.Serializable;


/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */

public class Credential implements Serializable {
    private String siteKey;

    private String src;
    private boolean relative;
    private boolean auth;
    private String authType;
    private String formMethod;
    private String siteUserNameField;
    private String sitePasswordField;
    private String hiddenVariables;
    private String htmlAttributes;


    public Credential() {
    }

    public static Credential getInstance(PortletPreferences prefs) {
        Credential credential = new Credential();

        credential.setSiteKey(prefs.getValue("site-key", ""));
        credential.setSrc(prefs.getValue("src", ""));
        credential.setRelative(Boolean.valueOf(prefs.getValue("relative", "false")));
        credential.setAuth(Boolean.valueOf(prefs.getValue("auth", "false")));
        credential.setAuthType(prefs.getValue("auth-type", ""));
        credential.setFormMethod(prefs.getValue("form-method", ""));
        credential.setSiteUserNameField(prefs.getValue("user-name-field", ""));
        credential.setSitePasswordField(prefs.getValue("password-field", ""));
        credential.setHiddenVariables(prefs.getValue("hidden-variables", ""));
        credential.setHtmlAttributes(prefs.getValue("html-attributes", ""));

        return credential;
    }

    public void store(PortletPreferences prefs) throws ValidatorException, IOException {
        try {
            prefs.setValue("site-key", getSiteKey());
            prefs.setValue("src", getSrc());
            prefs.setValue("relative", String.valueOf(isRelative()));
            prefs.setValue("auth", String.valueOf(isAuth()));
            prefs.setValue("auth-type", getAuthType());
            prefs.setValue("form-method", getFormMethod());
            prefs.setValue("user-name-field", getSiteUserNameField());
            prefs.setValue("password-field", getSitePasswordField());
            prefs.setValue("hidden-variables", getHiddenVariables());
            prefs.setValue("html-attributes", getHtmlAttributes());

            String[] htmlAttributes = getHtmlAttributes().split("\n");
            for (String attrib : htmlAttributes) {
                int pos = attrib.indexOf("=");

                if (pos != -1) {
                    String key = attrib.substring(0, pos);
                    String value = attrib.substring(pos + 1, attrib.length());

                    prefs.setValue(key, value);
                }
            }

            prefs.store();
        } catch (ReadOnlyException e) {
            e.printStackTrace();
        }
    }


    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public boolean isRelative() {
        return relative;
    }

    public void setRelative(boolean relative) {
        this.relative = relative;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getFormMethod() {
        return formMethod;
    }

    public void setFormMethod(String formMethod) {
        this.formMethod = formMethod;
    }

    public String getSiteUserNameField() {
        return siteUserNameField;
    }

    public void setSiteUserNameField(String siteUserNameField) {
        this.siteUserNameField = siteUserNameField;
    }

    public String getSitePasswordField() {
        return sitePasswordField;
    }

    public void setSitePasswordField(String sitePasswordField) {
        this.sitePasswordField = sitePasswordField;
    }

    public String getHiddenVariables() {
        return hiddenVariables;
    }

    public void setHiddenVariables(String hiddenVariables) {
        this.hiddenVariables = hiddenVariables;
    }

    public String getHtmlAttributes() {
        return htmlAttributes;
    }

    public void setHtmlAttributes(String htmlAttributes) {
        this.htmlAttributes = htmlAttributes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("siteKey", siteKey).
                append("src", src).
                append("relative", relative).
                append("auth", auth).
                append("authType", authType).
                append("formMethod", formMethod).
                append("siteUserNameField", siteUserNameField).
                append("sitePasswordField", sitePasswordField).
                append("hiddenVariables", hiddenVariables).
                append("htmlAttributes", htmlAttributes).
                toString();
    }
}
