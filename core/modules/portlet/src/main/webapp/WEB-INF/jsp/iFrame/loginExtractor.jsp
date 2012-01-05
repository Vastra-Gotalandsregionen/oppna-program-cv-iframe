<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<%--
  Created by IntelliJ IDEA.
  Created: 2011-12-27 12:35
  @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
--%>
<style type="text/css">
    .portlet-configuration fieldset {
        margin-bottom: 5px;
    }
</style>

<portlet:actionURL var="loginExtractorAction" name="loginExtractorAction"
                   portletMode="EDIT" secure="${portletConfig.sslUrlsOnly}"/>
<portlet:renderURL var="abort" portletMode="EDIT" secure="${portletConfig.sslUrlsOnly}"/>

<form:form method="post" action="${loginExtractorAction}" commandName="loginExtractor">
    <fieldset>
        <legend>Login-Form Information</legend>

        <table>
            <tr>
                <td>Login-Form URL: ${loginformUrl}</td>
            </tr>
        </table>

        <c:forEach var="form" items="${loginforms}" varStatus="formCnt">
            <fieldset>
                <legend>Form (${form.action})</legend>

                <table>
                    <tr>
                        <td>

                            <table class="lfr-table">
                                <tr>
                                    <form:hidden path="loginForms[${formCnt.index}].method"/>
                                    <form:hidden path="loginForms[${formCnt.index}].action"/>
                                    <td colspan="1"><form:checkbox path="loginForms[${formCnt.index}].use"/></td>
                                    <th colspan="8" align="left">Use this form</th>
                                </tr>
                                <tr>
                                    <td colspan="4"></td>
                                    <td>Method</td>
                                    <td colspan="4">${form.method}</td>
                                </tr>
                                <tr>
                                    <td colspan="4"></td>
                                    <td>Action</td>
                                    <td colspan="4">${form.action}</td>
                                </tr>
                                <tr>
                                    <td colspan="9">
                                        <hr/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Use</th>
                                    <th>Name</th>
                                    <th>Pass</th>
                                    <th>Dyna</th>
                                    <th>Extra</th>
                                    <th>type</th>
                                    <th>id</th>
                                    <th>name</th>
                                    <th>value</th>
                                    <th>options</th>
                                </tr>
                                <c:forEach var="formfield" items="${form.formFields}" varStatus="fieldCnt">
                                    <tr>
                                        <form:hidden
                                                path="loginForms[${formCnt.index}].loginFields[${fieldCnt.index}].fieldName"/>
                                        <form:hidden
                                                path="loginForms[${formCnt.index}].loginFields[${fieldCnt.index}].fieldValue"/>
                                        <td align="center">
                                            <form:checkbox
                                                    path="loginForms[${formCnt.index}].loginFields[${fieldCnt.index}].use"/>
                                        </td>
                                        <td align="center">
                                            <form:checkbox
                                                    path="loginForms[${formCnt.index}].loginFields[${fieldCnt.index}].nameField"/>
                                        </td>
                                        <td align="center">
                                            <form:checkbox
                                                    path="loginForms[${formCnt.index}].loginFields[${fieldCnt.index}].passwordField"/>
                                        </td>
                                        <td align="center">
                                            <form:checkbox
                                                    path="loginForms[${formCnt.index}].loginFields[${fieldCnt.index}].dynamicField"/>
                                        </td>
                                        <td align="center">
                                            <form:checkbox
                                                    path="loginForms[${formCnt.index}].loginFields[${fieldCnt.index}].extraField"/>
                                        </td>
                                        <td>${formfield.type}</td>
                                        <td>${formfield.id}</td>
                                        <td>${formfield.name}</td>
                                        <td>${formfield.value}</td>
                                        <td>
                                            <table>
                                                <c:forEach var="option" items="${formfield.options}">
                                                    <tr>
                                                        <td>${option.value}</td>
                                                        <td>${option.display}</td>
                                                    </tr>
                                                </c:forEach>
                                            </table>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </td>
                                </tr>
                                <tr>
                                    <td colspan="9">
                                        <hr/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </c:forEach>
        <fieldset>
            <legend>Action</legend>
            <table>
                <tr>
                    <td colspan="8">
                        <input type="submit" value="Update"/>
                        <a href="${abort}"><input type="button" value="Cancel"/></a>
                    </td>
                </tr>
            </table>
        </fieldset>
    </fieldset>
</form:form>
<fieldset>
    <legend>Login page source-view</legend>

    <table>
        <tr>
            <td>
                <c:out value="${loginformContent}"/>
            </td>
        </tr>
    </table>
</fieldset>
