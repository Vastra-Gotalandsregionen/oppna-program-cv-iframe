<%@ page import="com.liferay.portal.kernel.util.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="com.liferay.portlet.PortletPreferencesFactoryUtil" %>
<%@ page import="com.liferay.portal.util.PortalUtil" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<%
    /**
     * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
     *
     * Permission is hereby granted, free of charge, to any person obtaining a copy
     * of this software and associated documentation files (the "Software"), to deal
     * in the Software without restriction, including without limitation the rights
     * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
     * copies of the Software, and to permit persons to whom the Software is
     * furnished to do so, subject to the following conditions:
     *
     * The above copyright notice and this permission notice shall be included in
     * all copies or substantial portions of the Software.
     *
     * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
     * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
     * SOFTWARE.
     */
%>
                                              
<%--<%@ include file="/WEB-INF/jsp/iFrame/init.jsp" %>--%>

<html dir="<liferay-ui:message key='lang.dir' />">

<head>
    <meta content="no-cache" http-equiv="Cache-Control"/>
    <meta content="no-cache" http-equiv="Pragma"/>
    <meta content="0" http-equiv="Expires"/>
</head>

<body onLoad="setTimeout('document.fm.submit()', 100);">

<form action="${credential.src}" method="${credential.formMethod}" name="fm" id="fm">
    <h1>Hej Proxy</h1>

    <h1>User: ${siteCredential.siteUser}</h1>
    <input name="${credential.siteUserNameField}" type="hidden" value="${siteCredential.siteUser}"/>


    <h1>PW: ${siteCredential.sitePassword}</h1>
    <input name="${credential.sitePasswordField}" type="hidden" value="${siteCredential.sitePassword}"/>


</form>

</body>

</html>