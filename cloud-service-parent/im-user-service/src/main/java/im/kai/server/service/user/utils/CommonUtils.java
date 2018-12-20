package im.kai.server.service.user.utils;

import java.util.UUID;

/**
 * 公共类
 */
public class CommonUtils {

    /**
     * 生成随机的UUID
     * @return UUID
     */
    public static String randomValue() {

        return UUID.randomUUID().toString().replace("-", "") ;
    }


}
