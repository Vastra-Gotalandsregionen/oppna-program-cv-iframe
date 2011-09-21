package se.vgregion.portal.csiframe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.portal.csiframe.domain.SiteKey;
import se.vgregion.portal.csiframe.domain.persistence.SiteKeyRepository;
import se.vgregion.portal.csiframe.domain.UserSiteCredential;
import se.vgregion.portal.csiframe.domain.persistence.UserSiteCredentialRepository;

import java.util.List;

@Service
public class CredentialServiceImpl implements CredentialService {

    @Autowired
    private UserSiteCredentialRepository userSiteCredentialRepository;

    @Autowired
    private SiteKeyRepository siteKeyRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserSiteCredential getUserSiteCredential(String uid, String siteKey) {
        return userSiteCredentialRepository.getUserSiteCredential(uid, siteKey);
    }

    @Override
    public List<UserSiteCredential> getAllSiteCredentials(String uid) {
        return userSiteCredentialRepository.getAllSiteCredentials(uid);
    }

    @Override
    public List<SiteKey> getAllSiteKeys() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void save(UserSiteCredential siteCredential) {
        userSiteCredentialRepository.save(siteCredential);
    }

    @Transactional
    @Override
    public void save(SiteKey siteKey) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Transactional
    @Override
    public void remove(UserSiteCredential siteCredential) {
        userSiteCredentialRepository.remove(siteCredential);
    }

    @Override
    public void remove(SiteKey siteKey) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
