package im.kai.server.service.verificationcode.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 获取操作值
     * @return
     */
    public ValueOperations<String, String> getOps(){
        return redisTemplate.opsForValue();
    }

    /**
     * 获取验证码的缓存键值
     * @param mobile
     * @return
     */
    public String getVCodeCacheKey(String mobile){
        return "/kai/vcode/" + mobile;
    }

}
