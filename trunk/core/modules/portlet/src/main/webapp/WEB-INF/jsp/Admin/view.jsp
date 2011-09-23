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
    <c:forEach var="credential" items="${userCredentials}" varStatus="cnt">
        <portlet:actionURL var="updateCredential" name="updateCredential" portletMode="VIEW"/>
        <portlet:actionURL var="refreshCredential" name="refreshCredential" portletMode="VIEW"/>
        <fieldset>
            <legend>${credential.siteKey.title}</legend>
            <div class="values">
                <form method="POST" action="${updateCredential}"
                      name="credential_${cnt.index}"
                      id="credential_${cnt.index}">
                    <input id="credential_${cnt.index}.id"
                           name="id"
                           value="${credential.credential.id}"
                           type="hidden"/>
                    <input id="credential_${cnt.index}.uid"
                           name="uid"
                           value="${credential.credential.uid}"
                           type="hidden"/>
                    <input id="credential_${cnt.index}.siteKey"
                           name="siteKey"
                           value="${credential.credential.siteKey}"
                           type="hidden"/>
                    <table class="lfr-table">
                        <tr>
                            <td><label for="credential_${cnt.index}.siteUser">Användarnamn</label></td>
                            <td>
                                <input id="credential_${cnt.index}.siteUser"
                                       name="siteUser"
                                       value="${credential.credential.siteUser}"
                                       type="text"/>
                            </td>
                        </tr>
                        <tr>
                            <td><label for="credential_${cnt.index}.siteUser">Lösenord</label></td>
                            <td>
                                <input id="credential_${cnt.index}.sitePassword"
                                       name="sitePassword"
                                       value="${credential.credential.sitePassword}"
                                       type="password"/>
                            </td>
                        </tr>
                    </table>
                    <div class="buttons">
                        <input type="submit" value="Spara"/>
                        <a href="${refreshCredential}"><input type="button" value="Avbryt"/></a>
                    </div>
                </form>
            </div>
            <div class="description">
                <div class="portlet-msg-info">
                    <c:out value="${credential.siteKey.description}" escapeXml="false"/>
                </div>
            </div>
        </fieldset>

    </c:forEach>

</div>