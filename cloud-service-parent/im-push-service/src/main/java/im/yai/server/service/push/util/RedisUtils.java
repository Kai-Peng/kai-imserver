package im.kai.server.service.push.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/1 18:04
 * Redis 工具类
 */
@Component
public class RedisUtils {

    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     * @param pattern
     */
    @SuppressWarnings("unchecked")
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     * @param key
     */
    @SuppressWarnings("unchecked")
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 读取缓存
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object getValue(final String key) {
        return redisTemplate.opsForValue().get(key);
    }
    /**
     * 读取缓存
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object getList(final String key) {
        return redisTemplate.opsForList().leftPop(key);
    }
    /**
     * 读取缓存
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object getList(final String key,
                          long start,
                          long end) {
        return redisTemplate.opsForList().range(key,start,end);
    }
    /**
     * 读取缓存
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object getMap(final String key,
                         String hashKey) {
        return redisTemplate.opsForHash().get(key,hashKey);
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean setValue(final String key,
                            Object value) {
        return setValue(key,value,0);
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean setValue(final String key,
                            Object value,
                            long expireTime) {
        return setValue(key,value,expireTime,TimeUnit.SECONDS);
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean setValue(final String key,
                            Object value,
                            long expireTime,
                            TimeUnit unit) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            setExpireTime(key,expireTime,unit);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean setList(final String key,
                           Object value) {
        return setList(key,value,0);
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean setList(final String key,
                           Object value,
                           long expireTime) {
        return setList(key,value,expireTime,null);
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean setList(final String key,
                           Object value,
                           long expireTime,
                           TimeUnit unit) {
        boolean result = false;
        try {
            redisTemplate.opsForList().rightPush(key,value);
            setExpireTime(key,expireTime,unit);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean setMap(final String key,
                          Object hashKey,
                          Object value) {
        return setMap(key,hashKey,value,0);
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean setMap(final String key,
                          Object hashKey,
                          Object value,
                          long expireTime) {
        return setMap(key,hashKey,value,expireTime,null);
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean setMap(final String key,
                          Object hashKey,
                          Object value,
                          long expireTime,
                          TimeUnit unit) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().put(key,hashKey,value);
            setExpireTime(key,expireTime,unit);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置该Key的过期时间
     * @param key
     * @param expireTime
     */
    public void setExpireTime(String key,
                              long expireTime){
        setExpireTime(key,expireTime,null);

    }

    /**
     * 设置该Key的过期时间
     * @param key
     * @param expireTime
     * @param unit
     */
    public void setExpireTime(String key,
                              long expireTime,
                              TimeUnit unit){
        if (expireTime != 0) redisTemplate.expire(key, expireTime,
                unit == null ? TimeUnit.SECONDS : unit);
    }

    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

}