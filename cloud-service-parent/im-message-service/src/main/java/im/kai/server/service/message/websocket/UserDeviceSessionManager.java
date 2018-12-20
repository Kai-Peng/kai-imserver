package im.kai.server.service.message.websocket;

import im.kai.server.domain.user.DeviceType;
import im.kai.server.domain.websocket.WebSocketAuthToken;
import im.kai.server.service.message.protocol.WebSocketProtos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户设备会话管理器
 */
@Slf4j
@Component
public class UserDeviceSessionManager {
    /**
     *
     */
    private Map<String, Map<DeviceType, WebSocketSessionContext>> deviceSessionMap = new ConcurrentHashMap<>();

    @Autowired
    private MessageSendManager messageSendManager;

    @Autowired
    private OnlineUserDeviceManager onlineUserDeviceManager;

    /**
     * 注册某个会话，注册成功则会发起上线通知
     * @param context
     */
    public void register(WebSocketSessionContext context){
        WebSocketSessionContext offlineSession = null;

        Map<DeviceType, WebSocketSessionContext> map;
        WebSocketAuthToken authToken = context.getAuthToken();
        DeviceType deviceType = authToken.getDevice().getDeviceSubType().getDeviceType();

        synchronized (this) {
            if (deviceSessionMap.containsKey(authToken.getUserId())) {
                map = deviceSessionMap.get(authToken.getUserId());
                //如果存在则需要踢下线
                offlineSession = map.remove(deviceType);
            } else {
                map = new ConcurrentHashMap<>();
                deviceSessionMap.put(authToken.getUserId(), map);
            }
            map.put(deviceType, context);
        }

        if(offlineSession != null){
            //下线
            offline(offlineSession);
        }
        //上线
        online(context);
    }

    /**
     * 注销某个会话，会话在线则会发起下线通知
     * @param context
     */
    public void unregister(WebSocketSessionContext context){
        WebSocketAuthToken authToken = context.getAuthToken();
        DeviceType deviceType = authToken.getDevice().getDeviceSubType().getDeviceType();

        WebSocketSessionContext offlineSession = null;
        synchronized (this) {
            if (deviceSessionMap.containsKey(authToken.getUserId())) {
                Map<DeviceType, WebSocketSessionContext> map = deviceSessionMap.get(authToken.getUserId());
                //如果存在则需要踢下线
                offlineSession = map.remove(deviceType);
                if(map.size() == 0){
                    deviceSessionMap.remove(authToken.getUserId());
                }
            }
        }

        if(offlineSession != null){
            //下线
            offline(offlineSession);
        }

        //通知全局缓存注销session
        onlineUserDeviceManager.unregister(context);
    }

    /**
     * 某个设备上线
     * @param context
     */
    private void online(WebSocketSessionContext context){
        onlineUserDeviceManager.register(context);
    }

    /**
     * 某个设备下线
     * @param context
     */
    private void offline(WebSocketSessionContext context){
        messageSendManager.noticeMsgSend(context, WebSocketProtos.WebSocketMessage.Type.USER_OFFLINE);
    }

    /**
     * 获取用户的所有设备会话
     * @param userId
     * @return
     */
    @Nullable
    public Map<DeviceType, WebSocketSessionContext> getAll(String userId){
        return deviceSessionMap.getOrDefault(userId, null);
    }


    /**
     * 获取用户的某个设备会话
     * @param userId 用户id
     * @param deviceType 用户设备类型
     * @return
     */
    @Nullable
    public WebSocketSessionContext get(String userId, DeviceType deviceType){
        Map<DeviceType, WebSocketSessionContext> map = getAll(userId);
        return map == null ? null : map.getOrDefault(deviceType, null);
    }

    /**
     * 接收到MQ下线通知，同步本地缓存
     * @param userId
     * @param deviceTypeId
     * @return
     */
    public WebSocketSessionContext remove(String userId, int deviceTypeId){
        WebSocketSessionContext offlineSession = null;
        Map<DeviceType, WebSocketSessionContext> map;
        synchronized (this) {
            if (deviceSessionMap.containsKey(userId)) {
                map = deviceSessionMap.get(userId);
                //如果存在则需要踢下线
                offlineSession = map.remove(DeviceType.parse(deviceTypeId));
            }
        }
        return offlineSession;
    }
}
