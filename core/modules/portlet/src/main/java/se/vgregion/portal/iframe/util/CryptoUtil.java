package se.vgregion.portal.iframe.util;

import java.security.GeneralSecurityException;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface CryptoUtil {

    /**
     * Encrypt a value.
     *
     * @param value to be encrypted
     * @return encrypted value
     * @throws GeneralSecurityException Java security execption
     */
    String encrypt(String value) throws GeneralSecurityException;

    /**
     * Decrypt a value.
     *
     * @param value to be decrypted
     * @return decrypted value
     * @throws GeneralSecurityException Java security execption
     */
    String decrypt(String value) throws GeneralSecurityException;
}
