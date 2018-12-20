package im.kai.server.service.user.constant;

public interface RedisCache {

    /**专门缓存用户信息的键*/
    public String KEY_USER = "user";
    public String KEY_USER_PROFILE = "userprofile";
    public String KEY_USER_TOKEN = "token";
}
