package im.kai.server.service.message.websocket;

import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.utils.RedisKeyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 在线用户设备管理器
 */
@Service
public class OnlineUserDeviceManager {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Resource(name = "deviceRegisterRedisScript")
    private RedisScript deviceRegisterRedisScript;

    @Autowired
    private MessageSendManager messageSendManager;

    /**
     * 获取键值
     * @param userId
     * @return
     */
    private String getOnlineUserRedisKey(String userId){
        return RedisKeyBuilder.User.onlineKey(userId);
    }

    /**
     * 注册设备在线
     * @param context
     */
    public void register(WebSocketSessionContext context){
        List<String> keys = new ArrayList<>();
        keys.add(getOnlineUserRedisKey(context.getAuthToken().getUserId()));

        String deviceType = String.valueOf(context.getAuthToken().getDevice().getDeviceSubType().getDeviceType().getId());

        String oldServerId = redisTemplate.<String>execute(
                deviceRegisterRedisScript,
                stringRedisSerializer,
                stringRedisSerializer,
                keys, deviceType, context.getServerId());
        if(!StringUtils.isEmpty(oldServerId) && !context.getServerId().equals(oldServerId)){
            //有旧设备且不是当前服务器则需要踢下线，如设备是当前服务器则已在UserDeviceSessionManager处理掉了
            messageSendManager.noticeMsgSendByMQ(context, WebSocketProtos.WebSocketMessage.Type.USER_OFFLINE, oldServerId);
        }
    }

    /**
     * 注销设备在线状态
     * @param context
     */
    public void unregister(WebSocketSessionContext context){
        String userId = getOnlineUserRedisKey(context.getAuthToken().getUserId());
        String deviceType = String.valueOf(context.getAuthToken().getDevice().getDeviceSubType().getDeviceType().getId());
        redisTemplate.opsForHash().delete(userId, deviceType);
    }

    /**
     * 获取某个用户的所有在线设备
     * @param userId
     * @return key=设备类型, value=在线服务地址
     */
    public Map<String, String> getAll(String userId){
        return redisTemplate.<String, String>opsForHash().entries(getOnlineUserRedisKey(userId));
    }
}
