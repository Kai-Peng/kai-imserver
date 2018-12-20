package im.kai.server.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 加密、解密
 */
public class AesUtils {
  
    //编码方式
    public static final String CODE_TYPE = "UTF-8";

    //填充类型
    public static final String AES_TYPE = "AES/ECB/PKCS5Padding";
    /**
     * 加密
     *
     * @param text 需要加密的文本
     * @return 加密编码 后的字符串
     */
    public static byte[] encryptToBytes(String text , String key) throws Exception {

        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CODE_TYPE), "AES");
        Cipher cipher = Cipher.getInstance(AES_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(text.getBytes(CODE_TYPE));
    }
    /**
     * 加密 
     *  
     * @param text 需要加密的文本
     * @return 加密编码 后的字符串
     */  
    public static String encrypt(String text , String key) throws Exception {
        return Base64Utils.encodeToString(encryptToBytes(text, key));
    }

    /**
     * 解密
     *
     * @param encrypted
     * @return 解码解密后的字符串
     */
    public static String decrypt(String encrypted , String key) throws Exception {
        return decrypt(Base64Utils.decodeFromString(encrypted), key);

    }

    /**
     * 解密
     *
     * @param encryptedBytes
     * @return 解码解密后的字符串
     */
    public static String decrypt(byte[] encryptedBytes , String key) throws Exception {

        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CODE_TYPE), "AES");
        Cipher cipher = Cipher.getInstance(AES_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] original = cipher.doFinal(encryptedBytes);
        return new String(original , CODE_TYPE);

    }

} 