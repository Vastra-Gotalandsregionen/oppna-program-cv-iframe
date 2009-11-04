<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="liferay-theme" uri="http://liferay.com/tld/theme" %>

<%@ page import="com.liferay.portal.util.PortalUtil" %>
<%@ page import="com.liferay.portal.util.WebKeys" %>
<%@ page import="com.liferay.portlet.PortletPreferencesFactoryUtil" %>
<%@ page import="com.liferay.portlet.PortletURLUtil" %>
<%@ page import="javax.portlet.PortletMode" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="javax.portlet.PortletURL" %>
<%@ page import="javax.portlet.WindowState" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="com.liferay.portal.kernel.util.Validator" %>
<%@ page import="com.liferay.portal.kernel.util.StringPool" %>
<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>

<liferay-theme:defineObjects/>

<portlet:defineObjects/>

<%
    WindowState windowState = null;
    PortletMode portletMode = null;

    PortletURL currentURLObj = null;

    if (renderRequest != null) {
        windowState = renderRequest.getWindowState();
        portletMode = renderRequest.getPortletMode();

        currentURLObj = PortletURLUtil.getCurrent(renderRequest, renderResponse);
    } else if (resourceRequest != null) {
        windowState = resourceRequest.getWindowState();
        portletMode = resourceRequest.getPortletMode();

        //currentURLObj = PortletURLUtil.getCurrent(resourceRequest, resourceResponse);
    }

//String currentURL = currentURLObj.toString();
    String currentURL = PortalUtil.getCurrentURL(request);
%>

<%
PortletPreferences preferences = renderRequest.getPreferences();

String portletResource = ParamUtil.getString(request, "portletResource");

if (Validator.isNotNull(portletResource)) {
	preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
}

String src = preferences.getValue("src", StringPool.BLANK);
boolean relative = GetterUtil.getBoolean(preferences.getValue("relative", StringPool.BLANK));

    String vaultKey = preferences.getValue("vaultKey", StringPool.BLANK);
boolean auth = GetterUtil.getBoolean(preferences.getValue("auth", StringPool.BLANK));
String authType = preferences.getValue("auth-type", StringPool.BLANK);
String formMethod = preferences.getValue("form-method", StringPool.BLANK);
String userName = preferences.getValue("user-name", StringPool.BLANK);
String userNameField = preferences.getValue("user-name-field", StringPool.BLANK);
String password = preferences.getValue("password", StringPool.BLANK);
String passwordField = preferences.getValue("password-field", StringPool.BLANK);
String hiddenVariables = preferences.getValue("hidden-variables", StringPool.BLANK);

String border = preferences.getValue("border", "0");
String bordercolor = preferences.getValue("bordercolor", "#000000");
String frameborder = preferences.getValue("frameborder", "0");
String heightMaximized = preferences.getValue("height-maximized", "600");
String heightNormal = preferences.getValue("height-normal", "300");
String hspace = preferences.getValue("hspace", "0");
String scrolling = preferences.getValue("scrolling", "auto");
String vspace = preferences.getValue("vspace", "0");
String width = preferences.getValue("width", "100%");

List<String> iframeVariables = new ArrayList<String>();

Enumeration<String> enu = request.getParameterNames();

while (enu.hasMoreElements()) {
	String name = enu.nextElement();

	if (name.startsWith(_IFRAME_PREFIX)){
		StringBuilder sb = new StringBuilder();

		sb.append(name.substring(_IFRAME_PREFIX.length()));
		sb.append(StringPool.EQUAL);
		sb.append(request.getParameter(name));

		iframeVariables.add(sb.toString());
	}
}
%>

<%!
private static final String _IFRAME_PREFIX = "iframe_";
%>