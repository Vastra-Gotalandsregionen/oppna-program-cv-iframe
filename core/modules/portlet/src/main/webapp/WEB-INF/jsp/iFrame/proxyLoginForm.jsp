<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta content="no-cache" http-equiv="Cache-Control"/>
    <meta content="no-cache" http-equiv="Pragma"/>
    <meta content="0" http-equiv="Expires"/>
</head>

<body onLoad="setTimeout('document.fm.submit()', 100);">

<form action="${proxyFormAction}" method="${portletConfig.formMethod}" name="fm" id="fm">
    <h1>Autologin</h1>

    <p>
        Nyckel: ${siteCredential}<br/>
    </p>

    <input name="${portletConfig.siteUserNameField}" type="hidden" value="${siteCredential.siteUser}"/>
    <input name="${portletConfig.sitePasswordField}" type="hidden" value="${siteCredential.sitePassword}"/>

    <c:forEach var="entry" items="${portletConfig.hiddenVarialbleMap}">
        <input name="${entry.key}" value="${entry.value}" type="hidden"/>
    </c:forEach>

    <!-- input type="submit" / -->
</form>

</body>

</html>