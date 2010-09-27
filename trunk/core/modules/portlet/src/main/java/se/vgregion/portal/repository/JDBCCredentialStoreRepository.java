/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.portal.repository;

import java.security.GeneralSecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import se.vgregion.portal.csiframe.domain.UserSiteCredential;
import se.vgregion.portal.iframe.util.CryptoUtil;

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class JDBCCredentialStoreRepository extends SimpleJdbcDaoSupport implements CredentialStoreRepository {
    private static Logger log = LoggerFactory.getLogger(JDBCCredentialStoreRepository.class);

    private String dbCredentialFile;

    private CryptoUtil cryptoUtils;

    public void setCryptoUtils(CryptoUtil cryptoUtils) {
        this.cryptoUtils = cryptoUtils;
    }

    public void setDbCredentialFile(String dbCredentialFile) {
        this.dbCredentialFile = dbCredentialFile;
    }

    /**
     * Retrive user credentials. If no credentals are stored, null will be returned.
     * 
     * @param uid
     *            - user identifier.
     * @param siteKey
     *            - site credental identifier.
     * @return credentials
     */
    @Override
    public UserSiteCredential getUserSiteCredential(String uid, String siteKey) {
        String sql = "select * from vgr_user_site_credential where uid = ? and site_key = ?";

        RowMapper<UserSiteCredential> rowMapper = ParameterizedBeanPropertyRowMapper
                .newInstance(UserSiteCredential.class);
        UserSiteCredential creds = null;
        try {
            creds = getSimpleJdbcTemplate().queryForObject(sql, rowMapper, uid, siteKey);
            decryptSitePwd(creds);
        } catch (DataAccessException e) {
            log.info("Exception is leagal");
            return null;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        log.debug("User site credentials fetched from database: {}", creds);
        return creds;
    }

    /**
     * Store a credental. Password will be encrypted before storage.
     * 
     * @param siteCredential
     *            - credental to be stored
     */
    @Override
    public void addUserSiteCredential(UserSiteCredential siteCredential) {
        UserSiteCredential copy = siteCredential.copy();
        encryptSitePwd(copy);

        String sql = insertOrUpdateSql(copy);
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(copy);

        getSimpleJdbcTemplate().update(sql, parameterSource);
    }

    private void decryptSitePwd(UserSiteCredential creds) throws GeneralSecurityException {
        String encryptedPwd = creds.getSitePassword();
        String clearPwd = null;
        clearPwd = cryptoUtils.decrypt(encryptedPwd);
        creds.setSitePassword(clearPwd);
    }

    private void encryptSitePwd(UserSiteCredential siteCredential) {
        try {
            String clearPwd = siteCredential.getSitePassword();
            String encryptedPwd = cryptoUtils.encrypt(clearPwd);
            siteCredential.setSitePassword(encryptedPwd);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private String insertOrUpdateSql(UserSiteCredential siteCredential) {
        String sql;
        if (siteUserExists(siteCredential)) {
            sql = "UPDATE VGR_USER_SITE_CREDENTIAL "
                    + "SET SITE_USER = :siteUser,  SITE_PASSWORD = :sitePassword "
                    + "WHERE UID = :uid AND SITE_KEY = :siteKey";
        } else {
            sql = "INSERT INTO VGR_USER_SITE_CREDENTIAL " + "(UID, SITE_KEY, SITE_USER, SITE_PASSWORD) "
                    + "VALUES(:uid, :siteKey, :siteUser, :sitePassword)";
        }
        return sql;
    }

    private boolean siteUserExists(UserSiteCredential siteCredential) {
        String sql = "select count(*) from vgr_user_site_credential where uid = ? and site_key = ?";

        return getSimpleJdbcTemplate().queryForInt(sql, siteCredential.getUid(), siteCredential.getSiteKey()) > 0;
    }
}
