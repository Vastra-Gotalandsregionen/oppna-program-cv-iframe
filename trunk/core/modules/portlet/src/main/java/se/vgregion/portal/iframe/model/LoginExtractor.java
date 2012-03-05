package se.vgregion.portal.iframe.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-12-28 11:15
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class LoginExtractor {

    private List<LoginForm> loginForms;

    public List<LoginForm> getLoginForms() {
        return loginForms;
    }

    public void setLoginForms(List<LoginForm> loginForms) {
        this.loginForms = loginForms;
    }
}
