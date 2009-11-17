package se.vgregion.portal.iframe.util;

import com.sun.deploy.cache.LocalApplicationProperties;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Scanner;
import java.util.Locale;

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class CryptoUtilImpl implements CryptoUtil {

    private static final String AES = "AES";

    private File keyFile;

    public void setKeyFile(File keyFile) {
        this.keyFile = keyFile;
    }

    /**
     * Encrypt a value and generate a keyfile.
     * if the keyfile is not found then a new one is created
     *
     * @param value - value to be encrypted
     * @throws GeneralSecurityException - security exception
     * @return Encrypted value
     */
    public String encrypt(String value) throws GeneralSecurityException {
        if (!keyFile.exists()) {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            keyGen.init(128);
            SecretKey sk = keyGen.generateKey();
            FileWriter fw = null;
            try {
                fw = new FileWriter(keyFile);
                fw.write(byteArrayToHexString(sk.getEncoded()));
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        SecretKeySpec sks = getSecretKeySpec();

        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return byteArrayToHexString(encrypted);
    }

    /**
     * decrypt a value.
     *
     * @param value - value to be decrypted
     * @throws GeneralSecurityException - decrypt failed
     * @return decrypted value
     */
    public String decrypt(String value) throws GeneralSecurityException {
        SecretKeySpec sks = getSecretKeySpec();
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        byte[] cipherBytes = hexStringToByteArray(value);
        byte[] decrypted = cipher.doFinal(cipherBytes);

        return new String(decrypted);
    }


    private SecretKeySpec getSecretKeySpec() throws NoSuchAlgorithmException {
        byte[] key = readKeyFile();
        SecretKeySpec sks = new SecretKeySpec(key, AES);
        return sks;
    }

    private byte[] readKeyFile() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(keyFile).useDelimiter("\\Z");
            String keyValue = scanner.next();
            scanner.close();
            return hexStringToByteArray(keyValue);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    private byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    /**
     * Main method used for creating initial key file.
     *
     * @param args - not used
     */
    public static void main(String[] args) {
        final String KEY_FILE = "./howto.key";
        final String PWD_FILE = "./howto.properties";

        CryptoUtilImpl cryptoUtils = new CryptoUtilImpl();
        cryptoUtils.setKeyFile(new File(KEY_FILE));

        String clearPwd = "hittheroad";

        Properties p1 = new Properties();
        Writer w = null;
        try {
            p1.put("user", "liferay");
            String encryptedPwd = cryptoUtils.encrypt(clearPwd);
            p1.put("pwd", encryptedPwd);
            w = new FileWriter(PWD_FILE);
            p1.store(w, "");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // ==================
        Properties p2 = new Properties();
        Reader r = null;
        try {
            r = new FileReader(PWD_FILE);
            p2.load(r);
            String encryptedPwd = p2.getProperty("pwd");
            System.out.println(encryptedPwd);
            System.out.println(cryptoUtils.decrypt(encryptedPwd));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } finally {
          if (r != null) {
              try {
                  r.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
        }
    }
}
