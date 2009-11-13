package se.vgregion.portal.iframe.util;

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

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public final class CryptoUtils {

    private static final String AES = "AES";

    /**
     * Do not create any instance of this class.
     */
    private CryptoUtils() { }

    /**
     * Encrypt a value and generate a keyfile.
     * if the keyfile is not found then a new one is created
     *
     * @param value - value to be encrypted
     * @param keyFile - secret key file
     * @throws GeneralSecurityException - security exception
     * @throws FileNotFoundException - keyFile not found
     * @return Encrypted value
     */
    public static String encrypt(final String value, final File keyFile)
            throws GeneralSecurityException, FileNotFoundException {
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

        SecretKeySpec sks = getSecretKeySpec(keyFile);

        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return byteArrayToHexString(encrypted);
    }

    /**
     * decrypt a value.
     *
     * @param message - value to be decrypted
     * @param keyFile - secret key file
     * @throws GeneralSecurityException - decrypt failed
     * @throws FileNotFoundException - keyfile not found
     * @return decrypted value
     */
    public static String decrypt(String message, final File keyFile) throws GeneralSecurityException, FileNotFoundException {
        SecretKeySpec sks = getSecretKeySpec(keyFile);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        byte[] cipherBytes = hexStringToByteArray(message);
        byte[] decrypted = cipher.doFinal(cipherBytes);

        return new String(decrypted);
    }


    private static SecretKeySpec getSecretKeySpec(final File keyFile)
            throws NoSuchAlgorithmException, FileNotFoundException {
        byte[] key = readKeyFile(keyFile);
        SecretKeySpec sks = new SecretKeySpec(key, AES);
        return sks;
    }

    private static byte[] readKeyFile(File keyFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(keyFile).useDelimiter("\\Z");
        String keyValue = scanner.next();
        scanner.close();
        return hexStringToByteArray(keyValue);
    }


    private static String byteArrayToHexString(final byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    private static byte[] hexStringToByteArray(final String s) {
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

        String clearPwd = "hittheroad";

        Properties p1 = new Properties();
        Writer w = null;
        try {
            p1.put("user", "liferay");
            String encryptedPwd = encrypt(clearPwd, new File(KEY_FILE));
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
            System.out.println(decrypt(encryptedPwd, new File(KEY_FILE)));
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
