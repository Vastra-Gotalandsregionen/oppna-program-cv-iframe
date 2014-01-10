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

<%--
  Created by IntelliJ IDEA.
  User: David Rosell
  Date: Oct 28, 2009
  Time: 1:42:33 PM
--%>

<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/tld/vgr-regexp.tld" prefix="re" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<portlet:defineObjects/>

<%-- Only set secure flag if positive. If negative we leave it as it is, i.e. like the address bar in the browser --%>
<c:choose>
    <c:when test="${myPortletConfig.sslUrlsOnly}">
        <portlet:renderURL var="changeCredentials" secure="${myPortletConfig.sslUrlsOnly}">
            <portlet:param name="action" value="changeVaultCredentials"/>
        </portlet:renderURL>
    </c:when>
    <c:otherwise>
        <portlet:renderURL var="changeCredentials">
            <portlet:param name="action" value="changeVaultCredentials"/>
        </portlet:renderURL>
    </c:otherwise>
</c:choose>

<%-- Only set secure flag if positive. If negative we leave it as it is, i.e. like the address bar in the browser --%>
<c:choose>
    <c:when test="${myPortletConfig.sslUrlsOnly}">
        <portlet:renderURL portletMode="help" var="showHelp" secure="${myPortletConfig.sslUrlsOnly}"/>
    </c:when>
    <c:otherwise>
        <portlet:renderURL portletMode="help" var="showHelp"/>
    </c:otherwise>
</c:choose>

<portlet:resourceURL id="infoText" var="infoTextUrl"/>

<c:if test="${myPortletConfig.linkoutRedirect}">
    <meta http-equiv="refresh" content="7; url=${myPortletConfig.linkoutRedirectPage}">
</c:if>

<span class="vgr-portlet-controlls-container" style="${link_display}; float: left">
    <span class="vgr-portlet-controlls-left">
      <a title="Ändra inloggning" class="vgr-express-login" href="${changeCredentials}">Ändra inloggning</a>
    </span>
    <span class="vgr-portlet-controlls-right">
      <a class="vgr-portlet-help" href="${showHelp}" title="Hjälp">Hjälp</a>
    </span>
    <span>
        <a id="<portlet:namespace />preLogin" href="" target=""></a>
    </span>
    <span>
        <a id="<portlet:namespace />postLogin" href=""></a>
    </span>
    <span>
        <a id="<portlet:namespace />link" href=""></a>
    </span>
    <br/>
</span>
<span class="open-new-window" style="float: right">
    <a href="${iFrameSrc}" target="_blank" id="<portlet:namespace />openNewWindow">Öppna i nytt fönster</a>
</span>

<div id="<portlet:namespace />iframeWrap">
    <c:choose>
        <c:when test="${(empty myPortletConfig.allowedBrowsersRegExp) or re:matches(myPortletConfig.allowedBrowsersRegExp, header['user-agent'])}">
            <iframe name="<portlet:namespace />iframe"
                    id="<portlet:namespace />iframe"
                    border="${bordercolor}"
                    frameborder="${frameborder}"
                    height="${iFrameHeight}"
                    hspace="${hspace}"
                    scrolling="${scrolling}"
                    vspace="${vspace}"
                    width="${width}">
            </iframe>
        </c:when>
        <c:otherwise>
            <span class="error">${myPortletConfig.allowedBrowsersViolationMessage}</span>
            <br/><br/>
        </c:otherwise>
    </c:choose>
</div>

<script type="text/javascript">

    AUI().ready(
            'aui-base',
            'aui-loading-mask',
            function(A) {
                var iFrame = A.one('#<portlet:namespace />iframe');

                // ====== Check for pre-action otherwise load virtual login form ======
                var iFrameSrc = '${iFrameSrc}';
                var preIFrameSrc = '${preIFrameSrc}';
                var linkout = '${myPortletConfig.linkout}';
                var postRedirectUrl = '${postRedirectUrl}';
                var infoTextUrl = '${infoTextUrl}';
                if (preIFrameSrc != iFrameSrc) {
                    if (linkout == 'true') {
                        var linkId = '<portlet:namespace />preLogin';
                        var link = A.one('#'+linkId);
                        link.setAttribute('href', preIFrameSrc);
                        link.setAttribute('target', '${myPortletConfig.linkoutTarget}');
                        actuateLink(linkId, link);
                    } else {
                        iFrame.setAttribute('src', preIFrameSrc);
                    }
                } else {
                    // We want to be able to linkout also when there is no authentication to perform.
                    if (linkout == 'true') {
                        var linkId = '<portlet:namespace />link';
                        var link = A.one('#'+linkId);
                        link.setAttribute('href', iFrameSrc);
                        link.setAttribute('target', '${myPortletConfig.linkoutTarget}');
                        actuateLink(linkId, link);
                        iFrame.setAttribute('src', infoTextUrl);
                    } else {
                        iFrame.setAttribute('src', iFrameSrc);
                    }
                }
                // ======================

                // ====== Do the real login if a PreAction was configured ======
                A.later('500', iFrame, function() {
                    var iFrameSrc = '${iFrameSrc}';
                    var preIFrameSrc = '${preIFrameSrc}';
                    if (preIFrameSrc != iFrameSrc) {
                        iFrame.setAttribute('src', iFrameSrc);
                    }
                });
                // ======================

                // ====== Do linkout after login if a postLogin was configured ======
                var iFrame = A.one('#<portlet:namespace />iframe');
                A.later('2000', iFrame, function() {
                    if ('${myPortletConfig.authType}' == 'form') {
                        var postLogin = '${postLogin}';
                        var linkout = '${myPortletConfig.linkout}';
                        if (postLogin != 'null' && postLogin.length > 0) {
                            if (linkout == 'true') {
                                var linkId = '<portlet:namespace />postLogin';
                                var link = A.one('#'+linkId);
                                link.setAttribute('href', '${postLogin}');
                                link.setAttribute('target', '${myPortletConfig.linkoutTarget}');
                                actuateLink(linkId, link);
                            } else {
                                iFrame.setAttribute('src', '${postLogin}');
                            }
                        } else if (postRedirectUrl != 'null' && postRedirectUrl.length > 0) {
                            iFrame.setAttribute('src', postRedirectUrl);
                        }
                    }
                });
                // ======================

                A.one('#<portlet:namespace />openNewWindow').on('click', function (e) {
                    e.halt();
                    var newWind = window.open(e.target.get('href'));

                    var postRedirectUrl = '${postRedirectUrl}';
                    if (postRedirectUrl != 'null' && postRedirectUrl.length > 0) {
                        A.later('1000', iFrame, function() {
                            newWind.location = postRedirectUrl;
                        });
                    }

                });

                function actuateLink(linkId, auiLink) {
                    var link = document.getElementById(linkId);
                    if (link.click) {
                        link.click();
                        return;
                    } else {
                        auiLink.simulate('click');
                    }
                }

            }
    );

</script>