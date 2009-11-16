<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta content="no-cache" http-equiv="Cache-Control"/>
    <meta content="no-cache" http-equiv="Pragma"/>
    <meta content="0" http-equiv="Expires"/>
</head>

<body onLoad="setTimeout('document.fm.submit()', 100);">

<form action="${portletConfig.src}" method="${portletConfig.formMethod}" name="fm" id="fm">
    <h1>Hej Proxy</h1>

    <h1>User: ${siteCredential.siteUser}</h1>
    <input name="${portletConfig.siteUserNameField}" type="hidden" value="${siteCredential.siteUser}"/>


    <h1>PW: ${siteCredential.sitePassword}</h1>
    <input name="${portletConfig.sitePasswordField}" type="hidden" value="${siteCredential.sitePassword}"/>


</form>

</body>

</html>