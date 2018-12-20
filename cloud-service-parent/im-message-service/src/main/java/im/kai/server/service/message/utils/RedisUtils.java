package im.kai.server.service.message.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;



/**
 *Redis操作工具类
 */
@Repository
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T extends Serializable> void set(String key , T value) {

        redisTemplate.opsForValue().set(key, value);

    }
    public <T extends Serializable> void setInMinutes(String key , T value , long minutes) {

        redisTemplate.opsForValue().set(key, value , minutes , TimeUnit.MINUTES);

    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key) ;
    }
    public <T extends Serializable> void setInSeconds(String key , T value , long seconds) {

        redisTemplate.opsForValue().set(key, value , seconds , TimeUnit.SECONDS);

    }

    public void delete(String key){
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    /**
     * 从Redis获取全局的用户session信息（Redis缓存结构：HASH<key:UserId:<sub-key:DeviceId\value:sessionId+":"+本地IP地址>>)
     * @param key：userId
     * @return
     */
    public Map getHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 设置Redis全局的用户sesssion信息
     * @param userId
     * @param deviceId
     * @param sessionInfo
     */
    public void putHash(String userId, String deviceId, String sessionInfo) {
        redisTemplate.opsForHash().put(userId, deviceId, sessionInfo);
    }

    /**
     * 设置Redis全局的用户sesssion信息
     * @param userId
     * @param deviceIdSessionMap
     */
    public void putHashAll(String userId, Map deviceIdSessionMap) {
        redisTemplate.opsForHash().putAll(userId, deviceIdSessionMap);

    }

    /**
     * 删除hash key
     * @param userId
     */
    public void deleteHash(String userId, String... hashKeys) {
        redisTemplate.opsForHash().delete(userId, hashKeys);
    }
}