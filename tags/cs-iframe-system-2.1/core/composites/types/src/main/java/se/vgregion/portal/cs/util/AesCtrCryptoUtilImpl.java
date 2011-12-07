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

package se.vgregion.portal.cs.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;

/**
 * This implementation uses the AES algorithm with CTR block cipher mode to provide secure encryption.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 * @author <a href="mailto:patrik.bergstrom@knowit.se">Patrik Bergström</a>
 */
public class AesCtrCryptoUtilImpl implements CryptoUtil {

    private static final String AES = "AES";

    private static final String ALGORITHM = "AES/CTR/PKCS5Padding";

    private static final String CHARSET_NAME = "UTF-8";

    private static final int KEY_SIZE = 128;

    private File keyFile;

    /**
     * Constructor.
     *
     * @param keyFile file where the key is stored
     */
    public AesCtrCryptoUtilImpl(File keyFile) {
        this.keyFile = keyFile;
    }

    /**
     * Encrypt a value using the key from keyfile. If the keyfile is not found then a new one is created with a new
     * key.
     *
     * @param value value to be encrypted
     * @return Encrypted value
     * @throws java.security.GeneralSecurityException
     *          security exception
     */
    @Override
    public String encrypt(String value) throws GeneralSecurityException {
        if (!keyFile.exists()) {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            keyGen.init(KEY_SIZE);
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

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // Initialization vector - it is randomly generated and stored together with the encrypted password since
        // it does not need to be kept secret. E.g. it makes so that equal passwords will not be encrypted
        // equally and thus makes it harder for a potential attacker.
        byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();

        byte[] encrypted = new byte[0];
        try {
            encrypted = cipher.doFinal(value.getBytes(CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String encryptedHex = byteArrayToHexString(encrypted);
        String ivHex = byteArrayToHexString(iv);

        return encryptedHex + "-" + ivHex;
    }

    /**
     * Decrypts a value.
     *
     * @param value value to be decrypted
     * @return decrypted value
     * @throws GeneralSecurityException decrypt failed
     * @throws IllegalArgumentException if the <code>value</code> does not contain two parts divided by a dash (-)
     *                                  sign.
     */
    @Override
    public String decrypt(String value) throws GeneralSecurityException {
        String[] encPasswordPlusIv = value.split("-");

        if (encPasswordPlusIv.length != 2) {
            throw new IllegalArgumentException("The value must consist of a String with two parts separated by "
                    + " a dash (-) sign.");
        }

        String encPasswordHex = encPasswordPlusIv[0];
        String ivHex = encPasswordPlusIv[1];

        SecretKeySpec sks = getSecretKeySpec();

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(hexStringToByteArray(ivHex)));

        byte[] cipherBytes = hexStringToByteArray(encPasswordHex);
        byte[] decrypted = cipher.doFinal(cipherBytes);

        try {
            return new String(decrypted, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Won't happen");
        }
    }

    private SecretKeySpec getSecretKeySpec() throws NoSuchAlgorithmException {
        byte[] key = readKeyFile();
        SecretKeySpec sks = new SecretKeySpec(key, AES);
        return sks;
    }

    private byte[] readKeyFile() {
        Scanner scanner;
        try {
            scanner = new Scanner(keyFile).useDelimiter("\\Z");
            String keyValue = scanner.next();
            scanner.close();
            return hexStringToByteArray(keyValue);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        final int ffHex = 0xff;
        final int n16 = 16;
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & ffHex;
            if (v < n16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    private byte[] hexStringToByteArray(String s) {
        final int radix = 16;
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), radix);
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
        final String keyFile = "./howto.key";
        final String pwdFile = "./howto.properties";

        AesCtrCryptoUtilImpl cryptoUtils = new AesCtrCryptoUtilImpl(new File(keyFile));

        String clearPwd = "0123456789abcdef0123456789abcdef";

        Properties p1 = new Properties();
        Writer w = null;
        try {
            p1.put("user", "liferay");
            String encryptedPwd = cryptoUtils.encrypt(clearPwd);
            System.out.println(encryptedPwd);
            p1.put("pwd", encryptedPwd);
            w = new FileWriter(pwdFile);
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
            r = new FileReader(pwdFile);
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
