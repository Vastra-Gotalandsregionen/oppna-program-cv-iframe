package se.vgregion.portal.csiframe.service;

import se.vgregion.portal.csiframe.domain.SiteKey;
import se.vgregion.portal.csiframe.domain.UserSiteCredential;

import java.util.List;

public interface CredentialService {

    /**
     * Retrive user credentials. If no credentals are stored, null will be returned.
     * 
     * @param uid
     *            - user identifier.
     * @param siteKey
     *            - site credental identifier.
     * @return credentials
     */
    UserSiteCredential getUserSiteCredential(String uid, String siteKey);

    List<UserSiteCredential> getAllSiteCredentials(String uid);

    List<SiteKey> getAllSiteKeys();

    /**
     * Store a credental. Password will be encrypted before storage.
     * 
     * @param siteCredential
     *            - credental to be stored
     */
    void save(UserSiteCredential siteCredential);

    void save(SiteKey siteKey);

    void remove(UserSiteCredential siteCredential);

    void remove(SiteKey siteKey);
}
