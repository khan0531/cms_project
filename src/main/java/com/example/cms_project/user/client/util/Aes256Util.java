package com.example.cms_project.user.client.util;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

public class Aes256Util {
  public static String alg = "AES/CBC/PKCS5Padding";

  @Value("${aes.secret}")
  private static String AES_SECRET; // '_' 가 들어 가면 안된다.
  private static final String IV = AES_SECRET.substring(0, 16);

  public static String encrypt(String text) {
    try {
      Cipher cipher = Cipher.getInstance(alg);
      SecretKeySpec keySpec = new SecretKeySpec(AES_SECRET.getBytes(), "AES");
      IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
      cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
      byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
      return Base64.encodeBase64String(encrypted);
    } catch (Exception e) {
      return null;
    }
  }

  public static String decrypt(String cipherText) {
    try {
      Cipher cipher = Cipher.getInstance(alg);
      SecretKeySpec keySpec = new SecretKeySpec(AES_SECRET.getBytes(), "AES");
      IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
      cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
      byte[] decodedBytes = Base64.decodeBase64(cipherText);
      byte[] decrypted = cipher.doFinal(decodedBytes);
      return new String(decrypted, StandardCharsets.UTF_8);

    } catch (Exception e) {
      return null;
    }
  }
}
