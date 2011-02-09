package se.vgregion.portal.csiframe.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import se.vgregion.portal.core.domain.patterns.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The persistent class for the vgr_user_site_credential database table.
 */
@Entity
@Table(name = "vgr_user_site_credential",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"uid", "site_key"})
        })
public class UserSiteCredential extends AbstractEntity<UserSiteCredential, Long> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, length = 50, name = "uid")
    private String uid;

    @Column(nullable = false, length = 50, name = "site_key")
    private String siteKey;

    @Column(length = 256, name = "site_user")
    private String siteUser;

    @Column(length = 256, name = "site_password")
    private String sitePassword;

    /**
     * Empty default constructor.
     */
    public UserSiteCredential() {
    }

    /**
     * Constructor initialized with primary key fields.
     *
     * @param uid     user id.
     * @param siteKey name given of the administrator to site.
     */
    public UserSiteCredential(String uid, String siteKey) {
        setUid(uid);
        setSiteKey(siteKey);
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("uid", uid)
                .append("siteKey", siteKey).append("siteUser", siteUser).toString();
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
