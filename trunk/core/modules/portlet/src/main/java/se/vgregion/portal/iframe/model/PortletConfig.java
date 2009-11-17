package se.vgregion.portal.iframe.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;


/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */

public class PortletConfig implements Serializable {
    private static final long serialVersionUID = 526915899524612688L;

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

    private Map<String, String> htmlAttributeMap = new HashMap<String, String>();

    /**
     * Default constructor - used by Spring MCV.
     */
    public PortletConfig() {
    }

    /**
     * Factory for creating portlet configuration.
     *
     * @param prefs - PortletPreferences to synch with
     * @return configuration parameters
     */
    public static PortletConfig getInstance(PortletPreferences prefs) {
        PortletConfig portletConfig = new PortletConfig();

        portletConfig.setSiteKey(prefs.getValue("site-key", ""));
        portletConfig.setSrc(prefs.getValue("src", ""));
        portletConfig.setRelative(Boolean.valueOf(prefs.getValue("relative", "false")));
        portletConfig.setAuth(Boolean.valueOf(prefs.getValue("auth", "false")));
        portletConfig.setAuthType(prefs.getValue("auth-type", ""));
        portletConfig.setFormMethod(prefs.getValue("form-method", ""));
        portletConfig.setSiteUserNameField(prefs.getValue("user-name-field", ""));
        portletConfig.setSitePasswordField(prefs.getValue("password-field", ""));
        portletConfig.setHiddenVariables(prefs.getValue("hidden-variables", ""));
        portletConfig.setHtmlAttributes(prefs.getValue("html-attributes", ""));

        return portletConfig;
    }

    /**
     * Store configuration in PortlerPreferences.
     *
     * @param prefs - PortlerPreferences
     * @throws ValidatorException - property validation exception
     */
    public void store(PortletPreferences prefs) throws ValidatorException {
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

            prefs.store();
        } catch (ReadOnlyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convienience method to access html-attribute.
     *
     * @param attribute - attribute name
     * @param defaultValue - default value if attribute not found
     * @return attribute value
     */
    public String getHtmlAttribute(String attribute, String defaultValue) {
        String value = htmlAttributeMap.get(attribute);

        if (value == null || value.length() < 1) {
            return defaultValue;
        } else {
            return value;
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

    /**
     * Set html attributes.
     *
     * @param htmlAttributes - new line ('\n') separated list of html attributes
     */
    public void setHtmlAttributes(String htmlAttributes) {
        this.htmlAttributes = htmlAttributes;

        htmlAttributeMap.clear();
        String[] attribs = getHtmlAttributes().split("\n");
        for (String attrib : attribs) {
            int pos = attrib.indexOf("=");

            if (pos != -1) {
                String key = attrib.substring(0, pos);
                String value = attrib.substring(pos + 1, attrib.length());

                htmlAttributeMap.put(key, value);
            }
        }
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
