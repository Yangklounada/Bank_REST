package com.example.bankcards.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Утилита для шифрования и дешифрования конфиденциальных данных алгоритмом AES/GCM/NoPadding.
 * Первые 12 байт зашифрованных данных содержат IV; остальное — зашифрованная полезная нагрузка.
 */
@Component
public class EncryptionUtil {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    private final SecretKey secretKey;

    public EncryptionUtil(@Value("${app.encryption.secret}") String secret) {
        byte[] keyBytes = secret.getBytes();
        if (keyBytes.length < 16) {
            throw new IllegalArgumentException("Encryption key must be at least 16 bytes, got " + keyBytes.length);
        }
        byte[] rawKey = new byte[16];
        System.arraycopy(keyBytes, 0, rawKey, 0, 16);
        this.secretKey = new SecretKeySpec(rawKey, "AES");
    }

    /**
     * Шифрует текст алгоритмом AES/GCM/NoPadding. IV добавляется перед зашифрованными данными.
     */
    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom.getInstanceStrong().nextBytes(iv);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes());
            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
    /**
     * Дешифрует Base64-закодированный зашифрованный текст, содержащий IV в начале.
     */
    public String decrypt(String cipherText) {
        try {
            byte[] decoded = Base64.getDecoder().decode(cipherText);
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] encrypted = new byte[decoded.length - GCM_IV_LENGTH];
            System.arraycopy(decoded, 0, iv, 0, iv.length);
            System.arraycopy(decoded, iv.length, encrypted, 0, encrypted.length);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
            return new String(cipher.doFinal(encrypted));
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
