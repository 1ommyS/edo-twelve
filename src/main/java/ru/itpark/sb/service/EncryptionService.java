package ru.itpark.sb.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionService {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final String KEY_ALGORITHM = "AES";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final int SALT_LENGTH = 16;

    /*
    хэширвоание пароля: перевод его из оригинально вида в абракадабру
     */
    public String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception exception) {
            throw new RuntimeException("Не удалось захэшироват пароль");
        }
    }

    public boolean verifyPassword(String password, String passwordHash) {
        return passwordHash.equals(hashPassword(password));
    }

    private SecretKey generateKeyFromPassword(String password, byte[] salt) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] key = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(key, KEY_ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось сформировать ключ хэша");
        }
    }

    public byte[] encrypt(byte[] data, String password) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(salt);
            secureRandom.nextBytes(iv);

            SecretKey key = generateKeyFromPassword(password, salt);

            Cipher cipher = Cipher.getInstance(ALGORITHM); // AES/GCM/NoPadding
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

            // шифрование
            byte[] encryptedData = cipher.doFinal(data);

//            salt | iv | encryptedData
//            берем соль -> генерируем ключ пароля
//            берем iv - настраиваем шифр, то есть номер с которго было зашифровано, чтобы дешифровать
//            берем encryptedData и просто дешифруем

            byte[] result = new byte[SALT_LENGTH + GCM_IV_LENGTH + encryptedData.length];
            System.arraycopy(salt, 0, result, 0, SALT_LENGTH);
            System.arraycopy(iv, 0, result, SALT_LENGTH, GCM_IV_LENGTH);
            System.arraycopy(encryptedData, 0, result, SALT_LENGTH + GCM_IV_LENGTH, encryptedData.length);

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при шифровании данных");
        }
    }

    public byte[] decrypt(byte[] encryptedData, String password) {
        try {
            byte[] salt = new byte[SALT_LENGTH];
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] cipherText = new byte[encryptedData.length - SALT_LENGTH - GCM_IV_LENGTH];
            System.arraycopy(encryptedData, 0, salt, 0, SALT_LENGTH);
            System.arraycopy(encryptedData, SALT_LENGTH, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedData, SALT_LENGTH + GCM_IV_LENGTH, cipherText, 0, cipherText.length);

            var key = generateKeyFromPassword(password, salt);

            Cipher cipher = Cipher.getInstance(ALGORITHM); // AES/GCM/NoPadding
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

            return cipher.doFinal(cipherText);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Ошибка при дешифровке. Проверьте пароль");
        }
    }
}
