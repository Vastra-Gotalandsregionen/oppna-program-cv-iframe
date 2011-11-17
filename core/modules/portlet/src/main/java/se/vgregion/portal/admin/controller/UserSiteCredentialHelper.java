package se.vgregion.portal.admin.controller;

import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.UserSiteCredential;

import java.util.*;

public class UserSiteCredentialHelper {
    private final List<UserSiteCredential> userSiteCredentials;

    public UserSiteCredentialHelper(List<UserSiteCredential> value) {
        this.userSiteCredentials = value;
    }

    public UserSiteCredentialHelper(Collection<UserSiteCredential> value) {
        this(new ArrayList<UserSiteCredential>(value));
    }

    public List<UserSiteCredential> get() {
        return userSiteCredentials;
    }

    public UserSiteCredentialHelper orderBySiteKey() {
        Collections.sort(userSiteCredentials, new UserSiteCredentialComparator());
        return new UserSiteCredentialHelper(userSiteCredentials);
    }

    private static class UserSiteCredentialComparator implements Comparator<UserSiteCredential> {
        @Override
        public int compare(UserSiteCredential one, UserSiteCredential other) {
            return one.getSiteKey().compareTo(other.getSiteKey());
        }
    }

    public UserSiteCredentialHelper filterValid() {
        for (Iterator<UserSiteCredential> it = userSiteCredentials.iterator(); it.hasNext();) {
            UserSiteCredential siteKey = it.next();
            if (!siteKey.isValid()) it.remove();
        }
        return new UserSiteCredentialHelper(userSiteCredentials);
    }

}
