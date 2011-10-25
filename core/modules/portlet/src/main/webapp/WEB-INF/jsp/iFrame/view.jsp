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

<portlet:renderURL var="changeCredentials" secure="${myPortletConfig.sslUrlsOnly}">
    <portlet:param name="action" value="changeVaultCredentials"/>
</portlet:renderURL>
<portlet:renderURL portletMode="help" var="showHelp" secure="${myPortletConfig.sslUrlsOnly}"/>

<div class="vgr-portlet-controlls-container" style="${link_display}">
    <span class="vgr-portlet-controlls-left">
      <a title="Ändra inloggning" class="vgr-express-login" href="${changeCredentials}">Ändra inloggning</a>
    </span>
    <span class="vgr-portlet-controlls-right">
      <a class="vgr-portlet-help" href="${showHelp}" title="Hjälp">Hjälp</a>
    </span>
    <br/>
</div>

<div id="<portlet:namespace />iframeWrap">
    <c:choose>
        <c:when test="${(empty myPortletConfig.allowedBrowsersRegExp) or re:matches(myPortletConfig.allowedBrowsersRegExp, header['user-agent'])}">
            <iframe src="${preIFrameSrc}"
                    name="<portlet:namespace />iframe"
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
                // ====== LoadMask ======
                var iFrameWrap = A.one('#<portlet:namespace />iframeWrap');
                iFrameWrap.plug(A.LoadingMask, {
                    background: '#555',
                    strings: { loading: 'Laddar extern källa…' }
                });

                // Show loading mask
                iFrameWrap.loadingmask.show();

                // Hide loading mask after 2 seconds
                A.later('1500', iFrameWrap, function() {
                    iFrameWrap.loadingmask.hide();
                });
                // ======================

                // ====== Do the real login if a PreAction was configured ======
                var iFrame = A.one('#<portlet:namespace />iframe');
                A.later('500', iFrame, function() {
                    var iFrameSrc = '${iFrameSrc}';
                    var preIFrameSrc = '${preIFrameSrc}';
                    if (preIFrameSrc != iFrameSrc) {
                        iFrame.setAttribute('src', iFrameSrc);
                    }
                });
                // ======================
            }
    );

</script>