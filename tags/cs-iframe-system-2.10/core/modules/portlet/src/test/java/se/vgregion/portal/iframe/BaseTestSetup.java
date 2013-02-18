/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.portal.iframe;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;

/**
 * This action do that and that, if it has something special it is.
 * 
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class BaseTestSetup {
    protected void initPortletPreferences(PortletPreferences prefs) throws ReadOnlyException {
        prefs.setValue("site-key", "test-key");
        prefs.setValue("src", "test-src");
        prefs.setValue("relative", "false");
        prefs.setValue("auth", "true");
        prefs.setValue("auth-type", "form"); // basic or form
        prefs.setValue("form-method", "post"); // get or post
        prefs.setValue("user-name-field", "username");
        prefs.setValue("password-field", "password");
        prefs.setValue("hidden-variables", "test1=hidden1&test2=hidden2");
        prefs.setValue("html-attributes", "html1=apa\nhtml2=bepa");
    }
}
