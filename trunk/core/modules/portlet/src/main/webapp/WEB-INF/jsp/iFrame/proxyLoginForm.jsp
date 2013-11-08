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

<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta content="no-cache" http-equiv="Cache-Control"/>
    <meta content="no-cache" http-equiv="Pragma"/>
    <meta content="0" http-equiv="Expires"/>
</head>

<body onload="autoOpen();">

<c:choose>
    <c:when test="${portletConfig.linkout}">
        <c:choose>
            <c:when test="${portletConfig.authType == 'form'}">
                <form action="${proxyAction}"
                      method="${portletConfig.formMethod}"
                      target="${portletConfig.linkoutTarget}"
                      name="<portlet:namespace />proxy_login"
                      id="<portlet:namespace />proxy_login">
                    <input name="${portletConfig.siteUserNameField}" type="hidden" value="${siteCredential.siteUser}"/>
                    <c:choose>
                        <c:when test="${portletConfig.rdEncode}">
                            <input name="${portletConfig.sitePasswordField}" type="hidden" value="${rdPass}"/>
                        </c:when>
                        <c:otherwise>
                            <input name="${portletConfig.sitePasswordField}" type="hidden" value="${siteCredential.sitePassword}"/>
                        </c:otherwise>
                    </c:choose>

                    <c:forEach var="entry" items="${hiddenVariablesMap}">
                        <input name="${entry.key}" value="${entry.value}" type="hidden"/>
                    </c:forEach>
                    <c:forEach var="entry" items="${dynamicFieldValues}">
                        <input name="${entry.key}" value="${entry.value}" type="hidden"/>
                    </c:forEach>
                </form>
            </c:when>
            <c:otherwise>
                <a id="<portlet:namespace />proxy_login" name="<portlet:namespace />proxy_login" href="${proxyAction}" target="${portletConfig.linkoutTarget}"></a>
            </c:otherwise>
        </c:choose>

        <p>${siteKey.title} laddas i ett eget f&ouml;nster...</p>
        <c:if test="${postLoginLink}">
            <ul>
                <li>
                    Direkt l&auml;nk: <a href="${postLogin}" target="${portletConfig.linkoutTarget}">${postLogin}</a>
                </li>
            </ul>
        </c:if>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${portletConfig.authType == 'form'}">
                <form action="${proxyAction}"
                      method="${portletConfig.formMethod}"
                      name="<portlet:namespace />proxy_login"
                      id="<portlet:namespace />proxy_login">
                    <input name="${portletConfig.siteUserNameField}" type="hidden" value="${siteCredential.siteUser}"/>
                    <c:choose>
                        <c:when test="${portletConfig.rdEncode}">
                            <input name="${portletConfig.sitePasswordField}" type="hidden" value="${rdPass}"/>
                        </c:when>
                        <c:otherwise>
                            <input name="${portletConfig.sitePasswordField}" type="hidden" value="${siteCredential.sitePassword}"/>
                        </c:otherwise>
                    </c:choose>

                    <c:forEach var="entry" items="${hiddenVariablesMap}">
                        <input name="${entry.key}" value="${entry.value}" type="hidden"/>
                    </c:forEach>
                    <c:forEach var="entry" items="${dynamicFieldValues}">
                        <input name="${entry.key}" value="${entry.value}" type="hidden"/>
                    </c:forEach>
                </form>
            </c:when>
            <c:otherwise>
                <a id="<portlet:namespace />proxy_login" name="<portlet:namespace />proxy_login" href="${proxyAction}"></a>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>

</body>

<script type="text/javascript">
    function autoOpen() {
      if (!window.loginProxy) {
        window.loginProxy = document.getElementById('<portlet:namespace />proxy_login');
        if ("form" == "${portletConfig.authType}") {
            void(setTimeout('void(window.loginProxy.submit())', 200));
        } else {
            setTimeout(clickIfPossible(window.loginProxy), 100);
        }
      } else alert(12345);
    }

    function clickIfPossible(link) {
        var allowDefaultAction = true;
        alert('apa');
        alert(link);
        if (link.click) {
            link.click();
            return;
        } else if (document.createEvent) {
            var e = document.createEvent('MouseEvents');
            e.initEvent(
                    'click'     // event type
                    ,true      // can bubble?
                    ,true      // cancelable?
            );
            allowDefaultAction = link.dispatchEvent(e);
        }

        if (allowDefaultAction) {
            var f = document.createElement('form');
            f.action = link.href;
            document.body.appendChild(f);
            f.submit();
        }
    }
</script>
</html>