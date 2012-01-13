package se.vgregion.portal.cs.util;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Patrik Bergström
 */
public class AesCtrCryptoUtilImplTest {
    
    private File keyFile = new File("/tmp/notVerySecretKey");
    
    private CryptoUtil cryptoUtil = new AesCtrCryptoUtilImpl(keyFile);
    
    @Before
    public void setup() throws IOException {
        File tmpDir = new File("/tmp");
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        AesCtrCryptoUtilImpl.createKeyFile(keyFile);
    }
    
    @Test
    public void testEncryptAndDecrypt() throws Exception {
        String orgString = "asdf";
        
        String encrypted = cryptoUtil.encrypt(orgString);

        String decrypted = cryptoUtil.decrypt(encrypted);

        assertEquals(orgString, decrypted);
    }

}
