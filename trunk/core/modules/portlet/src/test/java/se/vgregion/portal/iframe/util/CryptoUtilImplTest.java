package se.vgregion.portal.iframe.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.io.File;
import java.security.GeneralSecurityException;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class CryptoUtilImplTest {
    CryptoUtilImpl cryptoUtil = new CryptoUtilImpl();
    File testKeyFile;

    @Before
    public void setUp() {
        testKeyFile = new File("./testKeyFile.key");
        cryptoUtil.setKeyFile(testKeyFile);
    }

    @After
    public void tearDown() {
        testKeyFile.delete();
    }

    @Test
    public void testEncryptDecrypt() throws GeneralSecurityException {
        String value = "test-encrypt-string";

        // encrypt
        String encrypted = cryptoUtil.encrypt(value);
        assertFalse("value not encrypyed", value.equals(encrypted));

        // decrypt
        String decrypted = cryptoUtil.decrypt(encrypted);
        assertEquals(value, decrypted);
    }
}
