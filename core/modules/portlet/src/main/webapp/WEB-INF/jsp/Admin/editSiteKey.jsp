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

<portlet:actionURL var="saveSiteKey" name="saveSiteKey" portletMode="EDIT" />
<portlet:actionURL var="deleteSiteKey" name="deleteSiteKey" portletMode="EDIT">
    <portlet:param name="siteKeyId" value="${currentSiteKey.id}"/>
</portlet:actionURL>
<portlet:renderURL var="abort" portletMode="EDIT"/>


<div class="cs-sitekey">
    <form:form method="POST" action="${saveSiteKey}" commandName="currentSiteKey" htmlEscape="false">
        <form:errors path="*" cssClass="portlet-msg-error"/>
        <form:hidden path="id"/>
        <table>
            <caption>
                <c:choose>
                    <c:when test="${currentSiteKey.id == null}">
                        Skapa en ny SiteKey
                    </c:when>
                    <c:otherwise>
                        Editera ${currentSiteKey.siteKey}
                    </c:otherwise>
                </c:choose>
            </caption>
            <tr>
                <td>Nyckel</td>
                <td><form:input path="siteKey"/> <form:errors path="siteKey" cssClass="portlet-msg-error"/></td>
            </tr>
            <tr>
                <td>Titel</td>
                <td><form:input path="title"/> <form:errors path="title" cssClass="portlet-msg-error"/></td>
            </tr>
            <tr>
                <td>Beskrivning (html är tillåtet)</td>
                <td><form:textarea cols="80" rows="5" path="description"/> <form:errors path="description"
                                                                      cssClass="portlet-msg-error"/></td>
            </tr>
            <tr>
                <td>Aktiv</td>
                <td><form:checkbox path="active"/></td>
            </tr>
        </table>
        <div>
            <input type="submit" value="Spara"/>
            <input type="button" value="Ta bort" onclick="confirmDelete('${deleteItSystem}');"/>
            <a href="${abort}"><input type="button" value="Avbryt"/></a>
        </div>
    </form:form>
</div>

<script type="text/javascript">
    <!--
     function confirmDelete(deleteUrl) {
        var confirmation = confirm('Nyckeln kommer att tas bort.\n\nVill du fortsätta?');

        if (!confirmation) {
            return false;
        }
        location.href=deleteUrl;
     }
     -->
</script>
