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

<fieldset>
    <legend>Login-Form Information</legend>

    <table class="lfr-table">
        <tr>
            <td>Login-Form URL: ${loginformUrl}</td>
        </tr>
        <tr>
            <td>
                <br/>
                <br/>
            </td>
        </tr>
        <c:forEach var="form" items="${loginforms}">
            <tr>
                <td>
                    <fieldset>
                        <legend>Form (${form.action})</legend>

                        <table class="lfr-table">
                            <tr>
                                <td>Method</td>
                                <td colspan="4">${form.method}</td>
                            </tr>
                            <tr>
                                <td>Action</td>
                                <td colspan="4">${form.action}</td>
                            </tr>
                            <tr>
                                <th>type</th>
                                <th>id</th>
                                <th>name</th>
                                <th>value</th>
                                <th>options</th>
                            </tr>
                            <c:forEach var="formfield" items="${form.formFields}">
                                <tr>
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
                        </table>
                    </fieldset>
                </td>
            </tr>
            <tr>
                <td>
                    <br/>
                    <br/>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td>
                ${loginformContent}<br/>
                ${error.message}
            </td>
        </tr>
        <tr>
            <td>
                <br/>
                <br/>
            </td>
        </tr>
        <tr>
            <td>
                <c:out value="${loginformContent}"/>
            </td>
        </tr>
        <tr>
            <td>
                <br/>
                <br/>
            </td>
        </tr>
    </table>
</fieldset>