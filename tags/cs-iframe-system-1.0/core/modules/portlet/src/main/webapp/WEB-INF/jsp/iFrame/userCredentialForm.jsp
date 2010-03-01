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

<portlet:actionURL var="formAction" />

<form:form method="post" action="${formAction}" commandName="siteCredential">
    <h1>SiteKey - ${siteCredential.siteKey}</h1>
    <form:hidden path="uid" />
    <form:hidden path="siteKey" />

    <label for="siteUser">Användarnamn</label>
    <form:input path="siteUser" id="siteUser" disabled="${portletConfig.screenNameOnly}"/>
    
    <label for="sitePassword">Lösenord</label>
    <form:password path="sitePassword" id="sitePassword" showPassword="true" />
    
    <input type="submit" value="Spara" />
    <input type="submit" value="Avbryt" name="_cancel" />
</form:form>
