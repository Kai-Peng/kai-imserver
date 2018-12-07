package im.kai.server.service.user.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;



/** 
* redis 缓存通用类
*/
@Repository
public class RedisUtils {
    
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置一个缓存
     * @param key 键
     * @param value 值
     * @param <T>
     */
    public <T extends Serializable> void set(String key , T value) {

        redisTemplate.opsForValue().set(key, value);

    }
    /**
     * 设置一个缓存
     * @param key 键
     * @param value 值
     * @param minutes 分钟数
     * @param <T>
     */
    public <T extends Serializable> void setInMinutes(String key , T value , long minutes) {

        redisTemplate.opsForValue().set(key, value , minutes , TimeUnit.MINUTES);

    }
    /**
     * 取一个缓存
     * @param key 键
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key) ;
    }

    /**
     * 设置一个缓存
     * @param key 键
     * @param value 值
     * @param seconds 秒数
     * @param <T>
     */
    public <T extends Serializable> void setInSeconds(String key , T value , long seconds) {

        redisTemplate.opsForValue().set(key, value , seconds , TimeUnit.SECONDS);

    }

    /**
     *  删除一个缓存
     * @param key 键
     */
    public void delete(String key){
        redisTemplate.opsForValue().getOperations().delete(key);
    }
}