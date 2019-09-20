package net.fzyz.jerryc05.fzyz_app.core.utils;

import android.annotation.SuppressLint;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class CryptoUtils {
  //  private static final String  ALGORITHM   = "AES";
//  public static final  int     Base64_FLAG = 2;
  private static final char[] KEY_CHARS = new char[]{
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
          'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
  };
//  private static final byte[]  KEY_SEED    = new byte[]{
//          22, 25, -35, -45, 25, 98, -55, -45, 10, 20, -45, 25, 26, -95, 25, -65, -11,
//          -99, 85, 45, -62, 10, 0, 11, -35, 48, -98, 65, -32, 14, -78, 25, 36, -56,
//          -45, -45, 12, 15, -35, -75, 15, -14, 62, -25, 33, -45, 55, 68, -88
//  };
//  public static final  String  TRANS_FORMATION = "AES/ECB/PKCS5Padding";

  public static final String ALGORITHM_AES = "AES";

  @StringDef(ALGORITHM_AES)
  @Retention(RetentionPolicy.SOURCE)
  @interface Algorithms {
  }

  //  public static String decrypt(final String s) throws NoSuchAlgorithmException {
//    final byte[]       bytes    = s.getBytes(AES.CHARSET);
//    final KeyGenerator instance = KeyGenerator.getInstance("AES");
//    instance.init(128, new SecureRandom(AES.KEY_SEED));
//    return decrypt(bytes, instance.generateKey());
//  }
//
//  public static String decrypt(final String s, final String s2) {
//    return decrypt(s.getBytes(AES.CHARSET), new SecretKeySpec(s2.getBytes(AES.CHARSET), "AES"));
//  }
//
//  public static String decrypt(final byte[] input, final SecretKey key) {
//    String s;
//    try {
//      final Cipher instance = Cipher.getInstance("AES/ECB/PKCS5Padding");
//      instance.init(2, key);
//      s = new String(instance.doFinal(input), AES.CHARSET);
//    }
//    catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
//      final Object o;
////      L.e(((Throwable)o).getLocalizedMessage(), new Object[0]);
//      s = "";
//    }
//    return s;
//  }
//
//  public static String decrypt(final byte[] array, final byte[] key) {
//    return decrypt(array, new SecretKeySpec(key, "AES"));
//  }
//
//  public static String decryptFromBase64(final String s) throws NoSuchAlgorithmException {
//    final byte[] decode = Base64.decode(s.getBytes(AES.CHARSET), 2);
//    final KeyGenerator instance = KeyGenerator.getInstance("AES");
//    instance.init(128, new SecureRandom(AES.KEY_SEED));
//    return decrypt(decode, instance.generateKey());
//  }
//
//  public static String decryptFromBase64(final String s, final String s2) {
//    return decrypt(Base64.decode(s.getBytes(AES.CHARSET), 2), s2.getBytes(AES.CHARSET));
//  }
//
//  public static String encrypt(final String s) throws NoSuchAlgorithmException {
//    final KeyGenerator instance = KeyGenerator.getInstance("AES");
//    instance.init(128, new SecureRandom(AES.KEY_SEED));
//    return encrypt(s.getBytes(AES.CHARSET), instance.generateKey());
//  }
//
  public static String encrypt(@NonNull final byte[] DATA, @NonNull final byte[] KEY,
                               @NonNull @Algorithms final String ALGORITHM) {
    return encrypt(DATA, new SecretKeySpec(KEY, ALGORITHM));
  }

  private static String encrypt(@NonNull final byte[] DATA,
                                @NonNull final SecretKey SECRET_KEY) {
    String s;
    try {
      @SuppressLint("GetInstance") final Cipher instance = Cipher.getInstance("AES/ECB/PKCS5Padding");
      instance.init(1, SECRET_KEY);
      s = new String(Base64.encode(instance.doFinal(DATA), 2));
    } catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
      final Object o;
//      L.e(((Throwable)o).getLocalizedMessage(), new Object[0]);
      s = "";
    }
    return s;
  }

  public static char[] getRandomKeyOf16() {
    final Random random = new Random(System.currentTimeMillis());
    final char[] key    = new char[16];

    for (int i = 0; i < 16; ++i)
      key[i] = KEY_CHARS[random.nextInt(16)];

    return key;
  }
}
