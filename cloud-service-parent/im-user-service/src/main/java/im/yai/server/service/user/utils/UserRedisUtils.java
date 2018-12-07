package im.kai.server.service.user.utils;

import im.kai.server.service.user.constant.RedisCache;
import im.kai.server.service.user.domain.dto.User;
import im.kai.server.service.user.domain.dto.UserProfile;
import im.kai.server.utils.TokenCacheUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 用户专用缓存
 */
@Slf4j
@Repository
public class UserRedisUtils {

    //缓存通用30分钟
    private final int CACHE_TIME_MINUTES = 60 * 24 * 7 ;
    private @Autowired RedisUtils redisUtils ;

    /**
     * 生成用户信息对应的键名
     * @param userId 用户ID
     * @param deviceId 设备ID
     * @return 生成的KEY
     */
    public String generateUserKey(String userId , String deviceId) {
        return RedisCache.KEY_USER.concat(String.valueOf(userId)).concat(deviceId) ;
    }

    /**
     * 生成用户详细信息的键名
     * @param userId
     * @param deviceId
     * @return
     */
    public String generateUserProfileKey(String userId , String deviceId) {
        return RedisCache.KEY_USER_PROFILE.concat(String.valueOf(userId)).concat(deviceId) ;
    }

    /**
     * 生成用户Token对应的键名
     * @param userId
     * @param deviceId
     * @return
     */
    public String generateUserTokenKey(String userId , String deviceId) {
        return RedisCache.KEY_USER_TOKEN.concat(String.valueOf(userId)).concat(deviceId) ;
    }

    /**
     * 缓存用户User 的信息
     * @param userId 用户ID
     * @param user User 实例
     * @param deviceId 设备ID
     */
    public void cacheUser(String userId , User user , String deviceId) {
        redisUtils.setInMinutes(generateUserKey(userId , deviceId) , user , CACHE_TIME_MINUTES);
    }

    /**
     * 缓存用户的详细信息
     * @param userId 用户ID
     * @param userProfile UserProfile 实例
     * @param deviceId 设备ID
     */
    public void cacheUserProfile(String userId , UserProfile userProfile, String deviceId) {
        redisUtils.setInMinutes(generateUserProfileKey(userId , deviceId) , userProfile , CACHE_TIME_MINUTES);
    }

    /**
     * 用户id和用户token 相互缓存
     * @param userId
     * @param token
     */
    public void cacheUserToken(String userId , String token , String deviceId) {

        String sxToken = TokenCacheUtils.getKey(token) ;

        log.info("sxToken : " + sxToken);
        redisUtils.setInMinutes(generateUserTokenKey(userId , deviceId) , sxToken , CACHE_TIME_MINUTES);
        redisUtils.setInMinutes(sxToken , userId , CACHE_TIME_MINUTES);
    }

    /**
     * 获取指定缓存数据
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getCache(String key) {
        return (T)redisUtils.get(key);
    }

    /**
     * 清除所有用户相关缓存
     * @param userId 用户ID
     * @param deviceId 设备ID
     */
    public void clearAllCache(String userId , String deviceId) {

        log.info("userId :" + userId + " , deviceId : " + deviceId);
        redisUtils.delete(generateUserKey(userId , deviceId));
        redisUtils.delete(generateUserProfileKey(userId, deviceId));

        String tokenKey = generateUserTokenKey(userId, deviceId) ;
        if(tokenKey != null) {
            String token = (String)redisUtils.get(tokenKey);
            if(token != null) {
                redisUtils.delete(token);
            }

            redisUtils.delete(tokenKey);
        }

    }

}
