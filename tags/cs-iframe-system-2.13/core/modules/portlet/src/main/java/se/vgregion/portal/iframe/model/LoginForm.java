package se.vgregion.portal.iframe.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-12-28 11:28
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class LoginForm {

    private boolean use;
    private String method;
    private String action;
    private List<LoginField> loginFields;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public List<LoginField> getLoginFields() {
        return loginFields;
    }

    public void setLoginFields(List<LoginField> loginFields) {
        this.loginFields = loginFields;
    }
}
