package se.vgregion.portal.iframe.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class UserSiteCredential implements Serializable {

    private String uid;
    private String siteKey;
    private String siteUser;
    private String sitePassword;

    /**
     * Default constructor - used by Spring MCV.
     */
    public UserSiteCredential() {}

    /**
     * Minimal constructor for creating a valid credental.
     *
     * @param uid - user identifier
     * @param siteKey - site identifier
     */
    public UserSiteCredential(String uid, String siteKey) {
        this.uid = uid;
        this.siteKey = siteKey;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    public String getSiteUser() {
        return siteUser;
    }

    public void setSiteUser(String siteUser) {
        this.siteUser = siteUser;
    }

    public String getSitePassword() {
        return sitePassword;
    }

    public void setSitePassword(String sitePassword) {
        this.sitePassword = sitePassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSiteCredential)) {
            return false;
        }

        UserSiteCredential that = (UserSiteCredential) o;

        if (!siteKey.equals(that.siteKey)) {
            return false;
        }
        if (!uid.equals(that.uid)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + siteKey.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("uid", uid).
                append("siteKey", siteKey).
                append("siteUser", siteUser).
                toString();
    }

    /**
     * Copy the UserSiteCredential object.
     *
     * @return new userSiteCredental object
     */
    public UserSiteCredential copy() {
        UserSiteCredential copy = new UserSiteCredential(uid, siteKey);
        copy.setSiteUser(siteUser);
        copy.setSitePassword(sitePassword);
        return copy;
    }
}
