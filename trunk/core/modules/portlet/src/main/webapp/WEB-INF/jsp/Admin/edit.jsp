<%--
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
--%>
<%@page session="false" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
        import="java.util.*,javax.portlet.*,se.vgregion.portal.cs.domain.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="/WEB-INF/fmt.tld" %>

<style type="text/css">
    <%@ include file="/style/csadmin-style.css"%>
</style>

<fmt:setBundle basename="se.vgregion.portal.cs.Texts"/>

<portlet:defineObjects/>

<portlet:renderURL var="addSiteKey" portletMode="EDIT">
    <portlet:param name="action" value="editSiteKey"/>
</portlet:renderURL>

<div class="cs-sitekey">
    <table>
        <tr class="cs-head">
            <th width="50">Id</th>
            <th width="100">Nyckel</th>
            <th width="150">Titel</th>
            <th width="150">Beskrivning</th>
            <th width="50">Aktiverad</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach var="siteKey" items="${siteKeys}" varStatus="cnt">
            <portlet:renderURL var="editSiteKey" portletMode="EDIT">
                <portlet:param name="action" value="editSiteKey"/>
                <portlet:param name="siteKeyId" value="${siteKey.id}"/>
            </portlet:renderURL>
            <portlet:actionURL var="deleteSiteKey" name="deleteSiteKey" portletMode="EDIT">
                <portlet:param name="siteKeyId" value="${siteKey.id}"/>
            </portlet:actionURL>


            <tr class="${(cnt.count % 2 == 0) ? 'cs-even' : 'cs-odd'}">
                <td>${siteKey.id}</td>
                <td>${siteKey.siteKey}</td>
                <td>${siteKey.title}</td>
                <td><c:out value="${siteKey.description}" escapeXml="true"/></td>
                <td>${siteKey.active}</td>
                <td><a href="${editSiteKey}">Ändra</a></td>
                <td><a href="${deleteSiteKey}">Ta bort</a></td>
            </tr>
        </c:forEach>
    </table>

    <a href="${addSiteKey}">Lägg till</a>

</div>