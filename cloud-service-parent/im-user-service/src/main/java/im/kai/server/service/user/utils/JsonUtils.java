package im.kai.server.service.user.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * JSON 转换工具类
 */
public class JsonUtils {

    public static String toString(Object object) {

        return JSONObject.toJSONString(object) ;

    }

}
