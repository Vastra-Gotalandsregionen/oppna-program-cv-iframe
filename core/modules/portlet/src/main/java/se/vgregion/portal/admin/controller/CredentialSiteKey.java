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
public class CredentialSiteKey implements Serializable {

    private UserSiteCredential credential;

    private SiteKey siteKey;

    public CredentialSiteKey() {
    }

    public CredentialSiteKey(UserSiteCredential credential, SiteKey siteKey) {
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
}
