<%--

    Copyright 2010 Västra Götalandsregionen

      This library is free software; you can redistribute it and/or modify
      it under the terms of version 2.1 of the GNU Lesser General Public
      License as published by the Free Software Foundation.

      This library is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU Lesser General Public License for more details.

      You should have received a copy of the GNU Lesser General Public
      License along with this library; if not, write to the
      Free Software Foundation, Inc., 59 Temple Place, Suite 330,
      Boston, MA 02111-1307  USA


--%>

<%--
  Created by IntelliJ IDEA.
  User: david
  Date: Nov 3, 2009
  Time: 2:59:58 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>


<style type="text/css">
    .portlet-configuration fieldset {
        margin-bottom: 5px;
    }
</style>

<portlet:actionURL var="formAction" escapeXml="false" secure="${portletConfig.sslUrlsOnly}"/>
<portlet:renderURL var="loginExtractor" secure="${portletConfig.sslUrlsOnly}">
    <portlet:param name="action" value="loginExtractor"/>
</portlet:renderURL>
<c:set var="ns"><portlet:namespace/></c:set>

<form:form method="post" action="${formAction}" commandName="portletConfig">
    <fieldset>
        <legend>General</legend>
        <table class="lfr-table">
            <tr>
                <td><label for="${ns}src">Source URL</label></td>
                <td><form:input path="src" id="${ns}src" size="80"/></td>
            </tr>
            <tr id="${ns}addUserLoggedIn">
                <td><label for="${ns}userLoggedIn">Add user logged in</label></td>
                <td><form:checkbox path="userLoggedIn" id="${ns}userLoggedIn"/></td>
            </tr>
        </table>
    </fieldset>

    <fieldset>
        <legend>Authentication</legend>

        <table class="lfr-table">
            <tr id="${ns}authCheckbox">
                <td><label for="${ns}auth">Authenticate</label></td>
                <td><form:checkbox path="auth" id="${ns}auth"/></td>
            </tr>
            <tr id="${ns}siteKey">
                <td><label for="${ns}siteKey">Site Key</label></td>
                <td>
                    <form:select path="siteKey"
                                 id="${ns}siteKeys">
                        <form:option value="">-- Ingen SiteKey --</form:option>
                        <c:forEach var="siteKey" items="${siteKeys}">
                            <form:option
                                    value="${siteKey.siteKey}">${siteKey.title} (${siteKey.siteKey})</form:option>
                        </c:forEach>
                    </form:select>
                </td>
            </tr>
            <tr id="${ns}authType">
                <td><label for="${ns}authTypeInput">Authentication Type</label></td>
                <td>
                    <form:select path="authType" id="${ns}authTypeInput">
                        <form:option value="form">Form</form:option>
                        <form:option value="basic">Basic</form:option>
                    </form:select>
                </td>
            </tr>

            <tr>
                <td><label for="${ns}sslUrlsOnlyField">SSL URLs Only</label></td>
                <td><form:checkbox path="sslUrlsOnly" id="${ns}sslUrlsOnly"/></td>
            </tr>
            <tr>
                <td><label for="${ns}preIFrameAction">Task before</label></td>
                <td><form:input path="preIFrameAction" id="${ns}preIFrameAction" size="80"/>(ie. logout)</td>
            </tr>
        </table>

        <fieldset>
            <legend>Link out</legend>

            <table class="lfr-table">
                <tr>
                    <td><label for="${ns}linkout">Active</label></td>
                    <td><form:checkbox path="linkout" id="${ns}linkout"/></td>
                </tr>
                <tr>
                    <td><label for="${ns}linkoutTarget">Target</label></td>
                    <td><form:input path="linkoutTarget" id="${ns}linkoutTarget" size="40"/></td>
                </tr>
                <tr>
                    <td><label for="${ns}linkoutRedirect">Redirect after link out</label></td>
                    <td><form:checkbox path="linkoutRedirect" id="${ns}linkoutRedirect"/></td>
                </tr>
                <tr>
                    <td><label for="${ns}linkoutRedirectPage">Redirect Page</label></td>
                    <td><form:input path="linkoutRedirectPage" id="${ns}linkoutRedirectPage" size="80"/></td>
                </tr>
            </table>
        </fieldset>

        <fieldset>
            <legend>Login form</legend>

            <table class="lfr-table">
                <tr id="${ns}formMethod">
                    <td><label for="${ns}formMethodInput">Form Method</label></td>
                    <td>
                        <form:select path="formMethod" id="${ns}formMethodInput">
                            <form:option value="post">POST</form:option>
                            <form:option value="get">GET</form:option>
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <td><label for="${ns}formActionField">Authentication Form Action</label></td>
                    <td><form:input path="formAction" id="${ns}formActionField" size="80"/>(default - Source URL)</td>
                </tr>
                <tr>
                    <td><label for="${ns}siteUserNameField">User Field Name</label></td>
                    <td><form:input path="siteUserNameField" id="${ns}siteUserNameField" size="80"/></td>
                </tr>
                <tr>
                    <td><label for="${ns}sitePasswordField">Password Field Name</label></td>
                    <td><form:input path="sitePasswordField" id="${ns}sitePasswordField" size="80"/></td>
                </tr>
                <tr>
                    <td><label for="${ns}hiddenVariables">Hidden Variables</label></td>
                    <td><form:input path="hiddenVariables" id="${ns}hiddenVariables" size="140"/></td>
                </tr>
            </table>

            <fieldset>
                <legend>Extract Login parameters</legend>

                <a href="${loginExtractor}"><input type="button" value="Login Extractor"/></a>
            </fieldset>
        </fieldset>

        <fieldset>
            <legend>Browser Compatibility</legend>

            <div class="portlet-msg-info" id="${ns}currentLoginMsg">
                <liferay-ui:message key="Regexp to validate against user-agent header sent from the client browser."/>
            </div>

            <table class="lfr-table">
                <tr>
                    <td><label for="${ns}allowedBrowsersRegExp">RegExp to validate allowed browsers</label>
                    </td>
                    <td><form:input path="allowedBrowsersRegExp" id="${ns}allowedBrowsersRegExp" size="80"/></td>
                </tr>
                <tr>
                    <td colspan="2">Example: If you want to ensure that the client is using Internet Explorer 5-8 then
                        type
                        in '.*MSIE [5678].*'
                    </td>
                </tr>
                <tr>
                    <td><label for="${ns}allowedBrowsersViolationMessage">Message to present if browser
                        RegExp fails</label></td>
                    <td><form:input path="allowedBrowsersViolationMessage" id="${ns}allowedBrowsersViolationMessage"
                                    size="80"/></td>
                </tr>
            </table>
        </fieldset>
    </fieldset>

    <fieldset>
        <legend>Advanced</legend>

        <table class="lfr-table">
            <tr>
                <td valign="top"><label for="${ns}htmlAttributes">HTML Attributes</label></td>
                <td>
                    <form:textarea path="htmlAttributes"
                                   id="${ns}htmlAttributes"
                                   wrap="soft"
                                   cols="80"
                                   rows="5"
                                   onKeyDown="Liferay.Util.checkTab(this); Liferay.Util.disableEsc();"/>
                </td>
            </tr>
        </table>
    </fieldset>

    <br/>

    <input type="submit" value="Save"/>
</form:form>