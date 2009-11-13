package se.vgregion.portal.repository;

import se.vgregion.portal.iframe.model.UserSiteCredential;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface CredentialVaultRepository {

    /**
     * Retrive credental.
     *
     * @param uid - user identifier
     * @param siteKey - site identifier
     * @return credentail
     */
    UserSiteCredential getUserSiteCredential(String uid, String siteKey);

    /**
     * Store credental.
     *
     * @param siteCredential - siteCredential
     */
    void addUserSiteCredential(UserSiteCredential siteCredential);
}
