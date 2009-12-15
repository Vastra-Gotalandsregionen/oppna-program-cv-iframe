<%--
  Created by IntelliJ IDEA.
  User: David Rosell
  Date: Oct 28, 2009
  Time: 1:42:33 PM
--%>

<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects/>
<script type="text/javascript">
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
</script>

<portlet:renderURL var="changeCredentials">
    <portlet:param name="action" value="changeVaultCredentials"/>
</portlet:renderURL>

<div style="${link_display}">
    <a class="express_login" href="${changeCredentials}">
        Ändra dina sparade inloggningsuppgifter
    </a>
    <br/>
</div>
<iframe src="${preIFrameSrc}"
        name="<portlet:namespace />iframe"
        border="${bordercolor}"
        frameborder="${frameborder}"
        height="${iFrameHeight}"
        hspace="${hspace}"
        scrolling="${scrolling}"
        vspace="${vspace}"
        width="${width}">
</iframe>
<script type="text/javascript">
    jQuery('iframe').load(function() {
        if (this.src != "${iFrameSrc}") {
            this.src="${iFrameSrc}";
        }
    });
</script>
