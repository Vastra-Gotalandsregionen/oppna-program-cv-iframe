<%
/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />
This is the <b>Sample JSP Portlet</b>. AABBCCDDEEFF Use this as a quick way to include JSPs. And more.

<%--
  Created by IntelliJ IDEA.
  User: David Rosell
  Date: Oct 28, 2009
  Time: 1:42:33 PM
--%>

<%@ include file="/WEB-INF/jsp/iFrame/init.jsp" %>

<%
    String iframeSrc = StringPool.BLANK;

    if (relative) {
        iframeSrc = themeDisplay.getPathContext();
    }

    iframeSrc += (String) request.getAttribute(WebKeys.IFRAME_SRC);

    String iframeHeight = heightNormal;

    if (windowState.equals(WindowState.MAXIMIZED)) {
        iframeHeight = heightMaximized;
    }
%>

<a href="${IFRAME_SRC}">Proxy Link [${IFRAME_SRC}]</a>
<iframe border="<%= border %>"
        bordercolor="<%= bordercolor %>"
        frameborder="<%= frameborder %>"
        height="<%= iframeHeight %>"
        hspace="<%= hspace %>"
        name="<portlet:namespace />iframe"
        scrolling="<%= scrolling %>"
        src="${IFRAME_SRC}"
        vspace="<%= vspace %>"
        width="<%= width %>">
</iframe>