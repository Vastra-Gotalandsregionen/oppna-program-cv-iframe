
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
