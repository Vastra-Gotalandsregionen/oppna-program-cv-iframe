
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<portlet:actionURL var="formAction" />

<form:form method="post" action="${formAction}" commandName="siteCredential">
    

    <label for="siteUser">Användarnamn</label>
    <form:input path="siteUser" id="siteUser" />
    
    <label for="sitePassword">Lösenord</label>
    <form:password path="sitePassword" id="sitePassword" />
    
    <input type="submit" value="Logga in" />
</form:form>
