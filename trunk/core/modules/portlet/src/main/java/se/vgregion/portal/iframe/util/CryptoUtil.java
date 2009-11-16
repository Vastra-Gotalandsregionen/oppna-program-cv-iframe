package se.vgregion.portal.iframe.util;

import java.security.GeneralSecurityException;
import java.io.FileNotFoundException;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface CryptoUtil {
    String encrypt(String value) throws GeneralSecurityException;
    String decrypt(String value) throws GeneralSecurityException;
}
