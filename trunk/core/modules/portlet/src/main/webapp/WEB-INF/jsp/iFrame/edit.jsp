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
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>


<style type="text/css">
	.portlet-configuration fieldset {
		margin-bottom: 5px;
	}
</style>

<portlet:actionURL var="formAction" escapeXml="false" secure="${portletConfig.sslUrlsOnly}"/>
<c:set var="ns"><portlet:namespace /></c:set>

<form:form method="post" action="${formAction}" commandName="portletConfig">
    <fieldset>
        <legend>General</legend>
        <table class="lfr-table">
            <tr>
                <td><label for="<portlet:namespace/>src">IFrame Source URL</label></td>
                <td><form:input path="src" id="${ns}src" size="80" /></td>
            </tr>
            <tr id="<portlet:namespace/>addUserLoggedIn">
                <td><label for="<portlet:namespace/>userLoggedIn">Add user logged in</label></td>
                <td><form:checkbox path="userLoggedIn" id="${ns}userLoggedIn" /></td>
            </tr>
            <!--tr>
                <td><label for="<portlet:namespace/>relative">Relativ to Context Path</label></td>
                <td><form:checkbox path="relative" id="${ns}relative" /></td>
            </tr-->
        </table>
    </fieldset>


    <fieldset>
        <legend>Authentication</legend>

        <div class="portlet-msg-info" id="<portlet:namespace/>currentLoginMsg">
		    <liferay-ui:message key="leave-the-user-name-and-password-fields-blank-to-use-your-current-login-information" />
	    </div>

        <table class="lfr-table">
            <tr id="<portlet:namespace/>authCheckbox">
                <td><label for="<portlet:namespace/>auth">Authenticate</label></td>
                <td><form:checkbox path="auth" id="${ns}auth" /></td>
            </tr>
            <tr id="<portlet:namespace/>siteKey">
                <td><label for="<portlet:namespace/>siteKey">Site Key</label></td>
                <td><form:input path="siteKey" id="${ns}siteKey" size="40" /></td>
            </tr>
            <tr id="<portlet:namespace/>authType">
                <td><label for="<portlet:namespace/>authTypeInput">Authentication Type</label></td>
                <td>
                    <form:select path="authType" id="${ns}authTypeInput">
                        <form:option value="basic">Basic</form:option>
                        <form:option value="form">Form</form:option>
                    </form:select>
                </td>
            </tr>

            <tbody id="<portlet:namespace/>formFields">
                <tr id="<portlet:namespace/>formMethod">
                    <td><label for="<portlet:namespace/>formMethodInput">Form Method</label></td>
                    <td>
                        <form:select path="formMethod" id="${ns}formMethodInput">
                            <form:option value="get">Get</form:option>
                            <form:option value="post">Post</form:option>
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <td><label for="<portlet:namespace/>formActionField">Authentication Form Action</label></td>
                    <td><form:input path="formAction" id="${ns}formActionField" size="80" /></td>
                </tr>
                <tr>
                    <td><label for="<portlet:namespace/>suggestScreenNameField">Suggest ScreenName</label></td>
                    <td><form:checkbox path="suggestScreenName" id="${ns}suggestScreenName" /></td>
                </tr>
                <tr>
                    <td><label for="<portlet:namespace/>screenNameOnlyField">ScreenName Only</label></td>
                    <td><form:checkbox path="screenNameOnly" id="${ns}screenNameOnly" /></td>
                </tr>
                <tr>
                    <td><label for="<portlet:namespace/>sslUrlsOnlyField">SSL URLs Only</label></td>
                    <td><form:checkbox path="sslUrlsOnly" id="${ns}sslUrlsOnly" /></td>
                </tr>
                <tr>
                    <td><label for="<portlet:namespace/>siteUserNameField">User Field Name</label></td>
                    <td><form:input path="siteUserNameField" id="${ns}siteUserNameField" size="80" /></td>
                </tr>
                <tr>
                    <td><label for="<portlet:namespace/>sitePasswordField">Password Field Name</label></td>
                    <td><form:input path="sitePasswordField" id="${ns}sitePasswordField" size="80" /></td>
                </tr>
                <tr>
                    <td><label for="<portlet:namespace/>hiddenVariables">Hidden Variables</label></td>
                    <td><form:input path="hiddenVariables" id="${ns}hiddenVariables" size="80" /></td>
                </tr>
                <tr>
                    <td><label for="<portlet:namespace/>preIFrameAction">Task before iFrame (ie. logout)</label></td>
                    <td><form:input path="preIFrameAction" id="${ns}preIFrameAction" size="80" /></td>
                </tr>
            </tbody>
        </table>
    </fieldset>
    
    <fieldset>
        <legend>Browser Compatibility</legend>

        <div class="portlet-msg-info" id="<portlet:namespace/>currentLoginMsg">
            <liferay-ui:message key="Regexp to validate against user-agent header sent from the client browser." />
        </div>

        <table class="lfr-table">
          <tr>
              <td><label for="<portlet:namespace/>allowedBrowsersRegExp">RegExp to validate allowed browsers</label></td>
              <td><form:input path="allowedBrowsersRegExp" id="${ns}allowedBrowsersRegExp" size="80" /></td>
          </tr>
          <tr>
              <td colspan="2">Example: If you want to ensure that the client is using Internet Explorer 5-8 then type in '.*MSIE [5678].*'</td>
          </tr>
          <tr>
              <td><label for="<portlet:namespace/>allowedBrowsersViolationMessage">Message to present if browser RegExp fails</label></td>
              <td><form:input path="allowedBrowsersViolationMessage" id="${ns}allowedBrowsersViolationMessage" size="80" /></td>
          </tr>
        </table>
    </fieldset>

    <fieldset>
        <legend>Advanced</legend>

        <table class="lfr-table">
            <tr>
                <td><label for="<portlet:namespace/>htmlAttributes">HTML Attributes</label></td>
                <td>
                    <form:textarea path="htmlAttributes" id="${ns}htmlAttributes" wrap="soft" onKeyDown="Liferay.Util.checkTab(this); Liferay.Util.disableEsc();"/>
                </td>
            </tr>
        </table>
    </fieldset>
    
    <br />

    <input type="submit" value="Save" />
</form:form>