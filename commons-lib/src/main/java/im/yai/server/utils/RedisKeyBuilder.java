package im.kai.server.utils;

/**
 * redis的缓存键值构造器
 */
public class RedisKeyBuilder {
    /**
     *
     * @param prefix
     * @param key
     * @return
     */
    public static String build(String prefix, String key){
        return "yai.im/" + prefix + "/" + key;
    }

    /**
     * 用户相关的数据
     */
    public static class User
    {
        /**
         * 登录token
         * @param token
         * @return
         */
        public static String tokenKey(String token){
            return build("token", token);
        }

        /**
         * 聊天在线
         * @param userId
         * @return
         */
        public static String onlineKey(String userId){
            return build("online", userId);
        }
    }
}
