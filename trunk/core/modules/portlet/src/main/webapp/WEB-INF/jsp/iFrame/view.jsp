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

<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery.blockUI.js"></script>
 
<portlet:defineObjects/>
<script type="text/javascript"><!--

    function <portlet:namespace />init() {
        var hash = document.location.hash;

        if ((hash != '#') && (hash != '')) {
            var src = '';

            var path = hash.substring(1);
            
            if (path.indexOf('http://') != 0) {
                src = '${baseSrc}';
            }

            src += path;

            var iframe = jQuery('#<portlet:namespace />iframe');

            iframe.attr('src', src);
        }
    }

    function <portlet:namespace />maximizeIframe(iframe) {
        var winHeight = 0;

        if (typeof(window.innerWidth) == 'number') {

            // Non-IE

            winHeight = window.innerHeight;
        }
        else if ((document.documentElement) &&
                 (document.documentElement.clientWidth || document.documentElement.clientHeight)) {

            // IE 6+

            winHeight = document.documentElement.clientHeight;
        }
        else if ((document.body) &&
                 (document.body.clientWidth || document.body.clientHeight)) {

                // IE 4 compatible

                winHeight = document.body.clientHeight;
            }

        // The value 139 here is derived (tab_height * num_tab_levels) +
        // height_of_banner + bottom_spacer. 139 just happend to work in
        // this instance in IE and Firefox at the time.

        iframe.height = (winHeight - 139);
    }

    function <portlet:namespace />monitorIframe() {
        var url = null;

        try {
            var iframe = document.getElementById('<portlet:namespace />iframe');

            url = iframe.contentWindow.document.location.href;
        }
        catch (e) {
            return true;
        }

        var baseSrc = '${baseSrc}';
        var iframeSrc = '${iFrameSrc}';

        if ((url == iframeSrc) || (url == iframeSrc + '/')) {
        }
        else if (Liferay.Util.startsWith(url, baseSrc)) {
            url = url.substring(baseSrc.length);

            <portlet:namespace />updateHash(url);
        }
        else {
            <portlet:namespace />updateHash(url);
        }

        return true;
    }

    function <portlet:namespace />resizeIframe() {
        var iframe = document.getElementById('<portlet:namespace />iframe');

        var height = null;

        try {
            height = iframe.contentWindow.document.body.scrollHeight;
        }
        catch (e) {
            <portlet:namespace />maximizeIframe(iframe);

            return true;
        }

        iframe.height = height + 50;

        return true;
    }

    function <portlet:namespace />updateHash(url) {
        document.location.hash = url;

        var maximize = jQuery('#p_p_id<portlet:namespace /> .portlet-maximize-icon a');

        if (maximize.length != 0) {
            var href = maximize.attr('href');

            if (href.indexOf('#') != -1) {
                href = href.substring(0, href.indexOf('#'));
            }

            maximize.attr('href', href + '#' + url);
        }

        var restore = jQuery('#p_p_id<portlet:namespace /> a.portlet-icon-back');

        if (restore.length != 0) {
            var href = restore.attr('href');

            if (href.indexOf('#') != -1) {
                href = href.substring(0, href.indexOf('#'));
            }

            restore.attr('href', href + '#' + url);
        }
    }

    jQuery(
            function() {
                <portlet:namespace />init();
            }
            );
--></script>

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

<div id="blockMe">  
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
    </c:otherwise>
  </c:choose>
</div>

<div id="blockDisplayMessage" style="display:none"> 
  <h1>&nbsp;Laddar extern källa...&nbsp;</h1> 
</div> 

<script type="text/javascript"><!--
    // Display "loading" block over IFrame for two seconds
    jQuery('#p_p_id<portlet:namespace /> #blockMe').block({ 
        message: jQuery('#p_p_id<portlet:namespace /> #blockDisplayMessage'),
        centerY: 50,
        centerX: 0,
        overlayCSS: {backgroundColor: '#EFEFEF' },
        fadeIn: 500, 
        fadeOut: 500, 
        timeout: 2000
    });

    // On load for IFrame...
    jQuery('#<portlet:namespace />iframe').load(function() {
        if (this.src != "${iFrameSrc}") {
            this.src="${iFrameSrc}";
        }
    });
    
    jQuery('#<portlet:namespace />iframe').ready(function() {
        jQuery('.vgr-express-login').fadeOut(3500);
    });
--></script>
