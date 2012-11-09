package se.vgregion.portal.admin.controller;

import se.vgregion.portal.cs.domain.UserSiteCredential;

import java.io.Serializable;
import java.util.*;

public class UserSiteCredentialHelper {
    private final List<UserSiteCredential> userSiteCredentials;

    /**
     * Constructor.
     *
     * @param value a list of {@link UserSiteCredential}s
     */
    public UserSiteCredentialHelper(List<UserSiteCredential> value) {
        this.userSiteCredentials = value;
    }

    /**
     * Constructor.
     *
     * @param value a collection of {@link UserSiteCredential}s
     */
    public UserSiteCredentialHelper(Collection<UserSiteCredential> value) {
        this(new ArrayList<UserSiteCredential>(value));
    }

    /**
     * Returns the {@link UserSiteCredential}s.
     *
     * @return the {@link UserSiteCredential}s
     */
    public List<UserSiteCredential> get() {
        return userSiteCredentials;
    }

    /**
     * Creates a new instance of {@link UserSiteCredentialHelper} where the order of {@link UserSiteCredential}s is
     * determined by their site keys.
     * 
     * @return a new instance of {@link UserSiteCredentialHelper}
     */
    public UserSiteCredentialHelper orderBySiteKey() {
        Collections.sort(userSiteCredentials, new UserSiteCredentialComparator());
        return new UserSiteCredentialHelper(userSiteCredentials);
    }

    private static class UserSiteCredentialComparator implements Comparator<UserSiteCredential>, Serializable {
        private static final long serialVersionUID = 9051962582752211347L;

        @Override
        public int compare(UserSiteCredential one, UserSiteCredential other) {
            return one.getSiteKey().compareTo(other.getSiteKey());
        }
    }

    /**
     * Creates a new instance of {@link UserSiteCredentialHelper} with only valid {@link UserSiteCredential}s.
     *
     * @return a new instance of {@link UserSiteCredentialHelper}
     */
    public UserSiteCredentialHelper filterValid() {
        for (Iterator<UserSiteCredential> it = userSiteCredentials.iterator(); it.hasNext();) {
            UserSiteCredential siteKey = it.next();
            if (!siteKey.isValid()) {
                it.remove();
            }
        }
        return new UserSiteCredentialHelper(userSiteCredentials);
    }

}
