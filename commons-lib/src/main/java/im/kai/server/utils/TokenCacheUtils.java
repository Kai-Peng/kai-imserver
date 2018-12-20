package im.kai.server.utils;

/**
 * token缓存处理
 */
public class TokenCacheUtils {
    /**
     * 获取缓存键值
     * @param token
     * @return
     */
    public static String getKey(String token){
        return "yai/token/" + token;
    }
}
