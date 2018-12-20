package im.kai.server.utils;

import org.springframework.util.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * HmacSHA1哈希计算工具类
 */
public abstract class HmacSHAUtils {

    /**
     * 采用HmacSHA1方式计算哈希
     * @param secretKey 密钥
     * @param data 数据
     * @return
     */
    public static byte[] hashToBytes(String secretKey, String data) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(secretKey.getBytes(), "HmacSHA1"));
        return mac.doFinal(data.getBytes("utf-8"));
    }

    /**
     * 采用HmacSHA1方式计算哈希，并返回base64编码后的数据
     * @param secretKey 密钥
     * @param data 数据
     * @return
     */
    public static String hashToString(String secretKey, String data) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        return Base64Utils.encodeToString(hashToBytes(secretKey, data));
    }
}
