package im.kai.server.service.message.websocket;

import im.kai.server.domain.websocket.WebSocketAuthToken;
import im.kai.server.service.message.controller.WSAuthenticatedInterceptor;
import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.message.WebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * websocket会话上下文封装对象
 */
@Slf4j
public class WebSocketSessionContext {
    private String sessionName;
    private WebSocketSession session;

    private String serverId;

    private WebSocketSessionContextManager contextManager;

    private WebSocketAuthToken authToken;
    private boolean closed;

    /**
     * 会话id
     * @return
     */
    public String getSessionName(){
        return sessionName;
    }
    /**
     * 获取WebSocketSession
     * @return
     */
    public WebSocketSession getSession() {
        return session;
    }

    /**
     * 获取管理者
     * @return
     */
    public WebSocketSessionContextManager getContextManager() {
        return contextManager;
    }

    /**
     * 获取当前服务器的标识id
     * @return
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * 获取ip地址表现形式
     * @param address
     * @return
     */
    private String getAddress(InetSocketAddress address){
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }
    /**
     * 初始化会话
     * @param session
     */
    public WebSocketSessionContext(WebSocketSession session, WebSocketSessionContextManager manager){
        this.contextManager = manager;
        this.session = session;
        this.authToken = (WebSocketAuthToken)session.getAttributes().get(WSAuthenticatedInterceptor.AUTH_DATA_KEY);
        this.sessionName = this.session.getId() + "(" + getAddress(this.session.getRemoteAddress()) + ")";
        this.serverId = getAddress(session.getLocalAddress());
        if(log.isDebugEnabled()) {
            log.debug("websocket会话[{}]已连接", this.sessionName);
        }
    }

    /**
     * 获取当前会话的授权token
     * @return
     */

    public WebSocketAuthToken getAuthToken() {
        return authToken;
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(WebSocketMessage message){
        try {
            this.session.sendMessage(message.build());
            if(log.isDebugEnabled()) {
                log.debug("websocket会话[{}]已发送消息 {}({})",
                        this.getSessionName(),
                        message.getId(),
                        message.getType());
            }
        } catch (IOException e) {
            log.error(String.format("websocket会话[%s]发送消息时发生错误", this.getSessionName()), e);
        }
    }


    /**
     * 关闭会话
     */
    public void close(){
        if(this.session.isOpen()) {
            try {
                this.session.close();
                if(log.isDebugEnabled()) {
                    log.debug("websocket会话[{}]服务端已主动关闭", this.getSessionName());
                }
            } catch (IOException e) {
                log.error(String.format("websocket会话[%s]关闭时发生错误", this.getSessionName()), e);
            }
        }
        if(!closed){
            this.notifySelfClosed();
        }
    }

    /**
     * 通知收到消息
     * @param message
     */
    void notifyMessageReceived(WebSocketProtos.WebSocketMessage message){
        if(log.isDebugEnabled()) {
            log.debug("websocket会话[{}]已接收到消息 {}({})",
                    this.getSessionName(),
                    message.getId(),
                    message.getType());
        }
    }

    /**
     * 通知已关闭
     */
    void notifyClosed(){
        if(log.isDebugEnabled()) {
            log.debug("websocket会话[{}]已关闭", this.getSessionName());
        }
        closed = true;
    }

    /**
     * 通知管理器本会话已关闭
     */
    private void notifySelfClosed(){
        this.contextManager.onClosed(this.session, new CloseStatus(100, "Closed"));
    }
}
