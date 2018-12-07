package im.kai.server.service.user.utils;

import im.kai.server.service.user.constant.TextCharset;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;


/**
 * Token 工具
 */
public class TokenUtils {

    /**
     * 创建token
     * @param key 密码
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    public static String createToken(byte[] key)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        Mac    mac                = Mac.getInstance("HmacSHA1");
        long   validUntilSeconds  = (System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)) / 1000;
        long   user               = Math.abs(new SecureRandom().nextInt());
        String userTime           = validUntilSeconds + ":"  + user;

        mac.init(new SecretKeySpec(key, "HmacSHA1"));
        return new String(Base64.encode(mac.doFinal(userTime.getBytes())) , "utf-8");

    }

    public static String createToken(String key)
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {

        return createToken(key.getBytes(TextCharset.UTF8));

    }
    public static void main(String args[])
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {

        System.out.println("token : " + createToken("12333".getBytes("utf-8")));
    }

}
