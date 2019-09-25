package net.fzyz.jerryc05.fzyz_app.core.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.GeneralSecurityException;
import java.util.Random;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

@SuppressWarnings("WeakerAccess")
public final class CryptoUtils {
  private static final String TAG = "CryptoUtils";

  private static final char[] KEY_CHARS = new char[]{
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
          'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

  public static final String ALGORITHM_AES_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding";
  public static final String ALGORITHM_XOR_CIPHER           = "X";

  @StringDef(ALGORITHM_AES_ECB_PKCS5PADDING)
  @Retention(RetentionPolicy.SOURCE)
  @interface Algorithm {
  }

  @NonNull
  public static byte[] encrypt(@NonNull final byte[] data, @NonNull final byte[] key,
                               @NonNull @Algorithm final String algorithm) {

    switch (algorithm) {
      case ALGORITHM_AES_ECB_PKCS5PADDING:
        final int slashIndex = algorithm.indexOf('/');
        //noinspection ConstantConditions
        return encryptCipher(data, new SecretKeySpec(key, slashIndex < 0
                ? algorithm : algorithm.substring(0, slashIndex)), algorithm);
      case ALGORITHM_XOR_CIPHER:
        return xorEnhanced(data, key);
      default:
        throw new IllegalArgumentException("Algorithm not found!");
    }
  }

  @SuppressLint("GetInstance")
  @NonNull
  private static byte[] encryptCipher(@NonNull final byte[] data,
                                      @NonNull final SecretKey secretKey,
                                      @NonNull @Algorithm final String algorithm) {
    try {
      final Cipher instance = Cipher.getInstance(algorithm);
      instance.init(Cipher.ENCRYPT_MODE, secretKey);
      return instance.doFinal(data);

    } catch (final GeneralSecurityException e) {
      Log.e(TAG, "encryptCipher: ", e);
      throw new IllegalStateException(e);
    }
  }

  private static byte[] xorEnhanced(@NonNull final byte[] data, @NonNull final byte[] key) {
    final int    dataLength = data.length;
    final byte[] result     = new byte[dataLength];
    for (int i = 0; i < dataLength; i++)
      result[i] = (byte) (data[i] ^ key[i % key.length] ^ i);
    return result;
  }

  @NonNull
  public static char[] generateRandomKeyFast(final int SIZE) {
    final Random random = new Random(System.currentTimeMillis());
    final char[] key    = new char[SIZE];

    for (int i = 0; i < SIZE; ++i)
      key[i] = KEY_CHARS[random.nextInt(SIZE)];

    return key;
  }

  public static boolean isAesNiSupported() throws IOException {
    final File cpuInfo = new File("/proc/cpuinfo");
    if (cpuInfo.exists())
      try (final BufferedReader bufferedReader =
                   new BufferedReader(new FileReader(cpuInfo))) {
        final Pattern pattern = Pattern.compile(
                "[^a-z]aes(?:[^a-z]|$)", CASE_INSENSITIVE);

        //noinspection MethodCallInLoopCondition
        for (String line; (line = bufferedReader.readLine()) != null; )
          if (pattern.matcher(line).find())
            return true;

      } catch (final IOException e) {
        Log.e(TAG, "getCPUInfo: ", e);
        throw e;
      }
    return false;
  }

//  private static final byte[]  KEY_SEED    = new byte[]{
//          22, 25, -35, -45, 25, 98, -55, -45, 10, 20, -45, 25, 26, -95, 25, -65, -11,
//          -99, 85, 45, -62, 10, 0, 11, -35, 48, -98, 65, -32, 14, -78, 25, 36, -56,
//          -45, -45, 12, 15, -35, -75, 15, -14, 62, -25, 33, -45, 55, 68, -88
//  };
//  public static final  String  TRANS_FORMATION = "AES/ECB/PKCS5Padding";

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
}
