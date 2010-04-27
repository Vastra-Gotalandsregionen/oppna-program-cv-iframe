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


<%@page pageEncoding="UTF-8"%>

<portlet:renderURL portletMode="view" var="showView" />
<br />
<div class="vgr-portlet-controlls-container" style="width: 600px; text-align: left;">
    <h1>Användning</h1>

    <p>Första gången man går in på sidan ges möjligheten att spara användarnamn och lösenord för en externa webplatsen.
    Dessa uppgifer krypteras, sparas och återanvänds så att du därefter loggas in automatiskt.</p>

    <p>Om du inte loggas in automatiskt eller behöver ändra inloggninsuppgifter, tryck på länken
    "Ändra inloggning" uppe till vänster och skriv in de korrekta inloggningsuppgifterna.</p>

    <span class="vgr-portlet-controlls-right">
        <a class="vgr-portlet-view" href="${showView}">Tillbaka</a>
    </span>
    
    <img alt="Hjälp" src="${pageContext.request.contextPath}/images/Question-Mark-Icon.png" />
</div>