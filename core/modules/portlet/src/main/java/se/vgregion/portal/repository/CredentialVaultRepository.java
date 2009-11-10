package se.vgregion.portal.repository;

import se.vgregion.portal.iframe.model.UserSiteCredential;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface CredentialVaultRepository {

    public UserSiteCredential getUserSiteCredential(String uid, String siteKey);

    void addUserSiteCredential(UserSiteCredential siteCredential);
}
