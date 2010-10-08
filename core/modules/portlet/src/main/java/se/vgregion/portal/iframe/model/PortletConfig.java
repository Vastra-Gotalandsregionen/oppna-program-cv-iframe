/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.portal.iframe.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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
    private String formAction;
    private boolean screenNameOnly;
    private boolean suggestScreenName = true;
    private boolean sslUrlsOnly;
    private String siteUserNameField;
    private String sitePasswordField;
    private String hiddenVariables;
    private String htmlAttributes;
    private String preIFrameAction;
    private String allowedBrowsersRegExp;
    private String allowedBrowsersViolationMessage;

    private Map<String, String> htmlAttributeMap = new HashMap<String, String>();

    /**
     * Default constructor - used by Spring MCV.
     */
    public PortletConfig() {
    }

    /**
     * Factory for creating portlet configuration.
     * 
     * @param prefs
     *            - PortletPreferences to synch with
     * @return configuration parameters
     */
    public static PortletConfig getInstance(PortletPreferences prefs) {
        PortletConfig portletConfig = new PortletConfig();

        portletConfig.setSiteKey(prefs.getValue("site-key", ""));
        portletConfig.setSrc(prefs.getValue("src", ""));
        portletConfig.setRelative(Boolean.valueOf(prefs.getValue("relative", "false")));
        portletConfig.setAuth(Boolean.valueOf(prefs.getValue("auth", "false")));
        portletConfig.setAuthType(prefs.getValue("auth-type", ""));
        portletConfig.setFormAction(prefs.getValue("form-action", ""));
        portletConfig.setFormMethod(prefs.getValue("form-method", ""));
        portletConfig.setScreenNameOnly(Boolean.valueOf(prefs.getValue("screenNameOnly", "false")));
        portletConfig.setSuggestScreenName(Boolean.valueOf(prefs.getValue("suggestScreenName", "true")));
        portletConfig.setSslUrlsOnly(Boolean.valueOf(prefs.getValue("sslUrlsOnly", "false")));
        portletConfig.setSiteUserNameField(prefs.getValue("user-name-field", ""));
        portletConfig.setSitePasswordField(prefs.getValue("password-field", ""));
        portletConfig.setHiddenVariables(prefs.getValue("hidden-variables", ""));
        portletConfig.setPreIFrameAction(prefs.getValue("pre-iframe-action", ""));
        portletConfig.setHtmlAttributes(prefs.getValue("html-attributes", ""));
        portletConfig.setAllowedBrowsersRegExp(prefs.getValue("allowed-browsers-regExp", ""));
        portletConfig.setAllowedBrowsersViolationMessage(prefs.getValue("allowed-browsers-violation-message", ""));
        return portletConfig;
    }

    /**
     * Store configuration in PortlerPreferences.
     * 
     * @param prefs
     *            - PortlerPreferences
     * @throws ValidatorException
     *             - property validation exception
     */
    public void store(PortletPreferences prefs) throws ValidatorException {
        try {
            prefs.setValue("site-key", getSiteKey());
            prefs.setValue("src", getSrc());
            prefs.setValue("relative", String.valueOf(isRelative()));
            prefs.setValue("auth", String.valueOf(isAuth()));
            prefs.setValue("auth-type", getAuthType());
            prefs.setValue("form-action", getFormAction());
            prefs.setValue("form-method", getFormMethod());
            prefs.setValue("screenNameOnly", String.valueOf(isScreenNameOnly()));
            prefs.setValue("suggestScreenName", String.valueOf(isSuggestScreenName()));
            prefs.setValue("sslUrlsOnly", String.valueOf(isSslUrlsOnly()));
            prefs.setValue("user-name-field", getSiteUserNameField());
            prefs.setValue("password-field", getSitePasswordField());
            prefs.setValue("hidden-variables", getHiddenVariables());
            prefs.setValue("pre-iframe-action", getPreIFrameAction());
            prefs.setValue("html-attributes", getHtmlAttributes());
            prefs.setValue("allowed-browsers-regExp", getAllowedBrowsersRegExp());
            prefs.setValue("allowed-browsers-violation-message", getAllowedBrowsersViolationMessage());

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
     * @param attribute
     *            - attribute name
     * @param defaultValue
     *            - default value if attribute not found
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

    public String getFormAction() {
        return formAction;
    }

    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    public String getFormMethod() {
        return formMethod;
    }

    public void setFormMethod(String formMethod) {
        this.formMethod = formMethod;
    }

    public boolean isScreenNameOnly() {
        return screenNameOnly;
    }

    public void setScreenNameOnly(boolean screenNameOnly) {
        this.screenNameOnly = screenNameOnly;
    }

    public boolean isSuggestScreenName() {
        return suggestScreenName;
    }

    public void setSuggestScreenName(boolean suggestScreenName) {
        this.suggestScreenName = suggestScreenName;
    }

    public boolean isSslUrlsOnly() {
        return sslUrlsOnly;
    }

    public void setSslUrlsOnly(boolean sslUrlsOnly) {
        this.sslUrlsOnly = sslUrlsOnly;
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

    public String getPreIFrameAction() {
        return preIFrameAction;
    }

    public void setPreIFrameAction(String preIFrameAction) {
        this.preIFrameAction = preIFrameAction;
    }

    /**
     * Convinience method for access of hidden attribures.
     * 
     * @return Map<String, String> - name/value pair
     */
    public Map<String, String> getHiddenVarialbleMap() {
        Map<String, String> staticHiddenMap = new HashMap<String, String>();

        String[] variables = hiddenVariables.split("&");
        for (String variable : variables) {
            String[] nameValue = variable.split("=", 2);
            if (nameValue.length > 1) {
                staticHiddenMap.put(nameValue[0], nameValue[1]);
            } else if (nameValue.length > 0) {
                staticHiddenMap.put(nameValue[0], "");
            }
        }

        return staticHiddenMap;
    }

    /**
     * Set html attributes.
     * 
     * @param htmlAttributes
     *            - new line ('\n') separated list of html attributes
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("siteKey", siteKey)
                .append("src", src).append("relative", relative).append("auth", auth).append("authType", authType)
                .append("formAction", formAction).append("formMethod", formMethod)
                .append("screenNameOnly", screenNameOnly).append("suggestScreenName", suggestScreenName)
                .append("sslUrlsOnly", sslUrlsOnly).append("siteUserNameField", siteUserNameField)
                .append("sitePasswordField", sitePasswordField).append("hiddenVariables", hiddenVariables)
                .append("preIFrameAction", preIFrameAction).append("htmlAttributes", htmlAttributes).toString();
    }

    public void setAllowedBrowsersRegExp(String allowedBrowsersRegExp) {
        this.allowedBrowsersRegExp = allowedBrowsersRegExp;
    }

    public String getAllowedBrowsersRegExp() {
        return allowedBrowsersRegExp;
    }

    public void setAllowedBrowsersViolationMessage(String allowedBrowsersViolationMessage) {
        this.allowedBrowsersViolationMessage = allowedBrowsersViolationMessage;
    }

    public String getAllowedBrowsersViolationMessage() {
        return allowedBrowsersViolationMessage;
    }
}
