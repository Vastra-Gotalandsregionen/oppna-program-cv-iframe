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
    <!--h1>Autologin</h1-->

    <!--p>
        Nyckel: ${siteCredential}<br/>
    </p-->

    <input name="${portletConfig.siteUserNameField}" type="hidden" value="${siteCredential.siteUser}"/>
    <input name="${portletConfig.sitePasswordField}" type="hidden" value="${siteCredential.sitePassword}"/>

    <c:forEach var="entry" items="${portletConfig.hiddenVarialbleMap}">
        <input name="${entry.key}" value="${entry.value}" type="hidden"/>
    </c:forEach>

    <!--input type="submit" /-->
</form>

</body>

</html>