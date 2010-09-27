package se.vgregion.portal.csiframe.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.portal.csiframe.domain.UserSiteCredential;
import se.vgregion.portal.csiframe.domain.UserSiteCredentialRepository;

@Service
public class UserSiteCredentialServiceImpl implements UserSiteCredentialService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSiteCredentialServiceImpl.class);

    @Autowired
    private UserSiteCredentialRepository userSiteCredentialRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserSiteCredential getUserSiteCredential(String uid, String siteKey) {
        return userSiteCredentialRepository.getUserSiteCredential(uid, siteKey);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void addUserSiteCredential(UserSiteCredential siteCredential) {
        userSiteCredentialRepository.addUserSiteCredential(siteCredential);
    }

}
