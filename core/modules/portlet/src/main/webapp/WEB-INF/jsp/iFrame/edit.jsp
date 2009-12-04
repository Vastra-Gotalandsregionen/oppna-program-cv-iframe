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

<script type="text/javascript">
	jQuery(
		function() {
			var authCheckbox = jQuery('#<portlet:namespace />authCheckbox');
			var auth = jQuery('#<portlet:namespace />auth');

			function toggleAuthOptions() {
				<%--var authType = jQuery('#<portlet:namespace />authType');--%>
				<%--var formFields = jQuery('#<portlet:namespace />formFields');--%>
				<%--var basicFields = jQuery('#<portlet:namespace />basicFields');--%>
				<%--var currentLoginMsg = jQuery('#<portlet:namespace />currentLoginMsg');--%>

				<%--if (auth.val() == 'true') {--%>
					<%--authType.show();--%>
					<%--currentLoginMsg.show();--%>

					<%--toggleAuthTypeOptions();--%>
				<%--}--%>
				<%--else {--%>
					<%--authType.hide();--%>
					<%--formFields.hide();--%>
					<%--basicFields.hide();--%>
					<%--currentLoginMsg.hide();--%>
				<%--}--%>
			}

			var authType = jQuery('select[@name=<portlet:namespace />authType]');

			function toggleAuthTypeOptions() {
				<%--var formFields = jQuery('#<portlet:namespace />formFields');--%>
				<%--var basicFields = jQuery('#<portlet:namespace />basicFields');--%>

				<%--if (authType.val() == 'form') {--%>
					<%--formFields.show();--%>
					<%--formFields.find('input').attr('disabled', false);--%>

					<%--basicFields.hide();--%>
					<%--basicFields.find('input').attr('disabled', true);--%>
				<%--}--%>
				<%--else {--%>
					<%--formFields.hide();--%>
					<%--formFields.find('input').attr('disabled', true);--%>

					<%--basicFields.show();--%>
					<%--basicFields.find('input').attr('disabled', false);--%>
				<%--}--%>
			}

			toggleAuthOptions();

			authCheckbox.click(
				function(event) {
					toggleAuthOptions();
				}
			);

			authType.change(
				function(event) {
					toggleAuthTypeOptions();
				}
			);
		}
	);
</script>

<portlet:actionURL var="formAction" />
<c:set var="ns"><portlet:namespace /></c:set>

<form:form method="post" action="${formAction}" commandName="portletConfig">
    <fieldset>
        <legend>General</legend>
        <table class="lfr-table">
            <tr>
                <td><label for="<portlet:namespace/>src">IFrame Source URL</label></td>
                <td><form:input path="src" id="${ns}src" size="80" /></td>
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
            </tbody>
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