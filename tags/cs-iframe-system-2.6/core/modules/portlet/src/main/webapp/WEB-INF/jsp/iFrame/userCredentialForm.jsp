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


<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
    <%@ include file="/style/csadmin-style.css"%>
</style>

<portlet:actionURL var="formAction" escapeXml="false" secure="${portletConfig.sslUrlsOnly}"/>

<div class="cs-admin">
    <h2>Ändra inloggning - ${siteKey.title}</h2>
    <fieldset>
        <div class="values">
            <form:form method="post" action="${formAction}" commandName="siteCredential">

                <form:hidden path="id"/>
                <form:hidden path="uid"/>
                <form:hidden path="siteKey"/>
                <table class="lfr-table">
                    <tr>
                        <td>
                            <label for="siteUser">Användarnamn</label>
                        </td>
                        <td>
                            <form:input path="siteUser" id="siteUser" disabled="${siteKey.screenNameOnly}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="sitePassword">Lösenord</label>
                        </td>
                        <td>
                            <form:password path="sitePassword" id="sitePassword" showPassword="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div class="buttons">
                                <input type="submit" value="Spara"/>
                                <input type="submit" value="Avbryt" name="_cancel"/>
                            </div>
                        </td>
                    </tr>
                </table>
            </form:form>
        </div>
        <div class="description">
            <div class="portlet-msg-info">
                <c:out value="${siteKey.description}" escapeXml="false"/>
            </div>
        </div>
    </fieldset>
</div>

<%@include file="helpContent.jsp" %>
