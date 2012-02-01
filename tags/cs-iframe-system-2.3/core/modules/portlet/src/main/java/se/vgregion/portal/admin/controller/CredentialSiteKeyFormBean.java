package se.vgregion.portal.admin.controller;

import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.UserSiteCredential;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 23/9-11
 * Time: 14:40
 */
public class CredentialSiteKeyFormBean implements Serializable {

    private static final long serialVersionUID = -2417191256232580482L;

    private UserSiteCredential credential;

    private SiteKey siteKey;

    public CredentialSiteKeyFormBean() {
    }

    public CredentialSiteKeyFormBean(UserSiteCredential credential, SiteKey siteKey) {
        this.credential = credential;
        this.siteKey = siteKey;
    }

    public UserSiteCredential getCredential() {
        return credential;
    }

    public void setCredential(UserSiteCredential credential) {
        this.credential = credential;
    }

    public SiteKey getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(SiteKey siteKey) {
        this.siteKey = siteKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CredentialSiteKeyFormBean that = (CredentialSiteKeyFormBean) o;

        if (credential != null ? !credential.equals(that.credential) : that.credential != null) return false;
        if (siteKey != null ? !siteKey.equals(that.siteKey) : that.siteKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = credential != null ? credential.hashCode() : 0;
        result = 31 * result + (siteKey != null ? siteKey.hashCode() : 0);
        return result;
    }
}
