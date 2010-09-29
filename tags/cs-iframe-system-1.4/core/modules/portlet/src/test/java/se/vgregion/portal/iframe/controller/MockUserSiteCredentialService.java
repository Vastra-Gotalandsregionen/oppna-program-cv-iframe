package se.vgregion.portal.iframe.controller;

import se.vgregion.portal.csiframe.domain.UserSiteCredential;
import se.vgregion.portal.csiframe.service.UserSiteCredentialService;

class MockUserSiteCredentialService implements UserSiteCredentialService {
    private int storeCalled = 0;

    public int getStoreCalled() {
        return storeCalled;
    }

    @Override
    public UserSiteCredential getUserSiteCredential(String uid, String siteKey) {
        if ("test-user".equals(uid) && "test-site-key".equals(siteKey)) {
            UserSiteCredential siteCredential = new UserSiteCredential("test-user", "test-site-key");
            siteCredential.setSiteUser("test-site-user");
            siteCredential.setSitePassword("test-site-password");

            return siteCredential;
        } else {
            return null;
        }
    }

    @Override
    public void addUserSiteCredential(UserSiteCredential siteCredential) {
        storeCalled++;
    }

}
