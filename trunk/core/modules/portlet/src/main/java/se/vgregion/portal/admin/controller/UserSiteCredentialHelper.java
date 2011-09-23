package se.vgregion.portal.admin.controller;

import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.UserSiteCredential;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class UserSiteCredentialHelper {
    private final List<UserSiteCredential> userSiteCredentials;

    public UserSiteCredentialHelper(List<UserSiteCredential> value) {
        this.userSiteCredentials = value;
    }

    public List<UserSiteCredential> get() {
        return userSiteCredentials;
    }

    public UserSiteCredentialHelper orderBySiteKey() {
        Collections.sort(userSiteCredentials, new Comparator<UserSiteCredential>() {
            @Override
            public int compare(UserSiteCredential one, UserSiteCredential other) {
                return one.getSiteKey().compareTo(other.getSiteKey());
            }
        });
        return new UserSiteCredentialHelper(userSiteCredentials);
    }

    public UserSiteCredentialHelper filterValid() {
        for (Iterator<UserSiteCredential> it = userSiteCredentials.iterator(); it.hasNext();) {
            UserSiteCredential siteKey = it.next();
            if (!siteKey.isValid()) it.remove();
        }
        return new UserSiteCredentialHelper(userSiteCredentials);
    }

}
