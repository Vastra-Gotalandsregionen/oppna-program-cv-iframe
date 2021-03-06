<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
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


<%@page pageEncoding="UTF-8" %>

<%-- Only set secure flag if positive. If negative we leave it as it is, i.e. like the address bar in the browser --%>
<c:choose>
    <c:when test="${myPortletConfig.sslUrlsOnly}">
        <portlet:renderURL portletMode="view" var="showView" secure="${portletConfig.sslUrlsOnly}"/>
    </c:when>
    <c:otherwise>
        <portlet:renderURL portletMode="view" var="showView"/>
    </c:otherwise>
</c:choose>
<%@include file="helpContent.jsp" %>
    <span class="vgr-portlet-controlls-right">
        <a class="vgr-portlet-view" href="${showView}">Tillbaka</a>
    </span>
