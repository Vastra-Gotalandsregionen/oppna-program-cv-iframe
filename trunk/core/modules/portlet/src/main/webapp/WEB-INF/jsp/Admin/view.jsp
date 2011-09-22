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

<div class="cs-admin">
    <table>
        <tr class="cs-head">
            <th width="100">Site Key</th>
            <th width="150">Site User</th>
            <th width="150">Site Password</th>
            <th width="50">Usable</th>
        </tr>
        <c:forEach var="credential" items="${userCredentials}" varStatus="cnt">
            <tr class="${(cnt.count % 2 == 0) ? 'cs-even' : 'cs-odd'}">
                <td>${credential.siteKey}</td>
                <td>${credential.siteUser}</td>
                <td>${credential.sitePassword}</td>
                <td>${credential.valid}</td>
            </tr>
        </c:forEach>
    </table>
</div>