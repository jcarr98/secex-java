package com.secex.encryption;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * A class to handle AES functionality within SecureExchange.
 * Key generation is done server side, so bytes will be loaded into
 * a key
 */
public class AES {
    private Cipher cipher;
    private SecretKey key;

    public AES() {
        // Create Cipher
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the current AES key for encryption/decryption. This method must be called before encrypting/decrypting
     * @param key The bytes to use to create a SecretKey
     */
    public void setKey(byte[] key) {
        this.key = new SecretKeySpec(key, "AES");
    }

    /**
     * Encrypt provided plaintext using already set AES key
     * @param plaintext The plaintext to encrypt
     * @return An array of arrays of bytes. The first array is the bytes used to create the Initialization Vector.
     * The second array is the encrypted bytes
     */
    public byte[][] encrypt(String plaintext) {
        byte[][] values = new byte[2][];

        // Decode plaintext into bytes
        byte[] decodedPlaintext = new byte[0];
        try {
            decodedPlaintext = plaintext.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        // Generate an IV and set up the Cipher to encrypt
        byte[] ivBytes = new byte[16];
        SecureRandom rand = new SecureRandom();
        rand.nextBytes(ivBytes);

        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        // Initialize cipher into encrypt mode and encrypt plaintext
        byte[] ciphertext;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            ciphertext = cipher.doFinal(decodedPlaintext);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }

        values[0] = ivBytes;
        values[1] = ciphertext;

        return values;
    }

    /**
     * Decrypt the given ciphertext into plaintext
     * @param ivBytes The bytes to use to create an Initialization Vector
     * @param ciphertext The bytes to decrypt
     * @return An unencrypted plaintext of the provided ciphertext
     */
    public String decrypt(byte[] ivBytes, byte[] ciphertext) {
        // Set up cipher to decrypt
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        // Initialize cipher into decrypt mode, get decoded bytes,
        // and turn byte array into plaintext String
        String plaintext;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] decodedPlaintext = cipher.doFinal(ciphertext);
            plaintext = new String(decodedPlaintext, "UTF-8");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        return plaintext;
    }
}
