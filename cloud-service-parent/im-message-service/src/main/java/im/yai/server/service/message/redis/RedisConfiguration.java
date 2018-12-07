package im.kai.server.service.message.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import im.kai.server.exception.ServerErrorException;
import im.kai.server.utils.JarUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.io.IOException;

/**
 * Redis的Lua 脚本文件加载类
 */
@Configuration
public class RedisConfiguration {
    /**
     *
     * @param factory
     * @return
     */
    @Bean
    @Primary
    public StringRedisTemplate redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =  new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 读取lua脚本内容
     * @param pathName
     * @return
     */
    private String readLuaScript(String pathName){
        try {
            return JarUtils.getResourceTextFile(RedisConfiguration.class, pathName);
        } catch (IOException e) {
            throw new ServerErrorException("加载redis lua脚本文件出错: " + pathName, e);
        }
    }

    /**
     * 用于设备注册的lua脚本
     * @return
     */
    @Bean
    @Qualifier("deviceRegisterRedisScript")
    public RedisScript deviceRegisterRedisScript(){
        return RedisScript.of(readLuaScript("lua/device_register.lua"), String.class);
    }

}
