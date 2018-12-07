package im.kai.server.utils;

import java.security.SecureRandom;

/**
 * 随机数工具类
 */
public class RandomUitls {
    /**
     * 随机生成一个6位数的验证码
     * @return
     */
    public static String generate6RndNumber(){
        SecureRandom random = new SecureRandom();
        int randomInt       = 100000 + random.nextInt(900000);
        return String.valueOf(randomInt);
    }
}
