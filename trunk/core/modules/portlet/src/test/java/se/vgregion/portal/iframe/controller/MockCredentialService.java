package se.vgregion.portal.iframe.controller;

import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.service.CredentialService;

import java.util.List;

class MockCredentialService implements CredentialService {
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
    public List<UserSiteCredential> getAllSiteCredentials(String uid) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<SiteKey> getAllSiteKeys() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SiteKey getSiteKey(Long siteKeyId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SiteKey getSiteKey(String siteKey) {
        return new SiteKey("apa", "ApA", "Apa describe", true, false, true);
        // use File |
        // Settings | File
        // Templates.
    }

    @Override
    public void save(UserSiteCredential siteCredential) {
        storeCalled++;
    }

    @Override
    public void save(SiteKey siteKey) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void remove(UserSiteCredential siteCredential) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void remove(Long siteKeyId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
