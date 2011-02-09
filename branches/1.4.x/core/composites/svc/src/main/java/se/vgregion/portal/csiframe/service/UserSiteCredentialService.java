package se.vgregion.portal.csiframe.service;

import se.vgregion.portal.csiframe.domain.UserSiteCredential;

public interface UserSiteCredentialService {

    /**
     * Retrive user credentials. If no credentals are stored, null will be returned.
     * 
     * @param uid
     *            - user identifier.
     * @param siteKey
     *            - site credental identifier.
     * @return credentials
     */
    public UserSiteCredential getUserSiteCredential(String uid, String siteKey);

    /**
     * Store a credental. Password will be encrypted before storage.
     * 
     * @param siteCredential
     *            - credental to be stored
     */
    public void addUserSiteCredential(UserSiteCredential siteCredential);
}
