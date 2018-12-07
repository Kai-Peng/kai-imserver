package im.kai.server.service.message.websocket;

import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.event.SessionClosedEvent;
import im.kai.server.service.message.websocket.event.SessionConnectedEvent;
import im.kai.server.service.message.websocket.event.SessionMessageReceivedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WebSocketSessionContextManager {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    UserDeviceSessionManager userDeviceSessionManager;

    //本地缓存定义
    private Map<String, WebSocketSessionContext> contextMap = new ConcurrentHashMap<>();

    /**
     * 判断本地contextMap缓存是否包含该session id
     * @param id
     * @return
     */
    boolean has(String id){
        return contextMap.containsKey(id);
    }
    /**
     *通过session id获取session上下文对象
     * @param session
     * @return
     */
    @Nullable
    public WebSocketSessionContext get(WebSocketSession session){
        return has(session.getId()) ? contextMap.get(session.getId()) : null;
    }

    /**
     *通过session id字符串获取session上下文对象
     * @param sessionId
     * @return
     */
    @Nullable
    public WebSocketSessionContext get(String sessionId){
        return has(sessionId) ? contextMap.get(sessionId) : null;
    }

    /**
     * 获取用户设备会话管理器
     * @return
     */
    public UserDeviceSessionManager getUserDeviceSessionManager() {
        return userDeviceSessionManager;
    }

    /**
     * 连接
     * @param session
     */
    public void onConnected(WebSocketSession session){
        if(has(session.getId())){
            return;
        }
        WebSocketSessionContext context;
        synchronized (this){
            context = new WebSocketSessionContext(session, this);
            contextMap.put(session.getId(), context);
        }
        //注册设备
        this.userDeviceSessionManager.register(context);
        applicationContext.publishEvent(new SessionConnectedEvent(context));
    }

    /**
     * 处理消息
     * @param session
     * @param message
     */
    public void handleBinaryMessage(WebSocketSession session, WebSocketProtos.WebSocketMessage message){
        if(!has(session.getId())){
            //丢掉了连接信息？
            onConnected(session);
        }
        WebSocketSessionContext context = contextMap.get(session.getId());
        context.notifyMessageReceived(message);
        applicationContext.publishEvent(new SessionMessageReceivedEvent(context, message));
    }

    /**
     * 某个会话已关闭连接
     * @param session websocket会话
     * @param status 关闭状态
     */
    public void onClosed(WebSocketSession session, CloseStatus status){
        if(has(session.getId())){
            WebSocketSessionContext context = contextMap.remove(session.getId());
            //注销设备
            this.userDeviceSessionManager.unregister(context);
            context.notifyClosed();
            applicationContext.publishEvent(new SessionClosedEvent(context));
        }
    }

    /**
     * 接收到MQ下线通知，同步本地缓存
     * @param sessionId
     */
    public void removeContextMap(String sessionId){
        contextMap.remove(sessionId);
    }
}
