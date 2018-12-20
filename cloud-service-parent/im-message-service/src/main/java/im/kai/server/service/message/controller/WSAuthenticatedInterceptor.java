package im.kai.server.service.message.controller;

import im.kai.server.domain.websocket.WebSocketAuthToken;
import im.kai.server.service.message.utils.UserServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * websocket授权拦截器
 * 检查传递的token值
 */
@Slf4j
@Service
public class WSAuthenticatedInterceptor implements HandshakeInterceptor {
    /**
     * 数据加密密匙
     */
    @Value("${kai.service.websocket.secretKey}")
    private String secretKey;
    /**
     * 数据签名密匙
     */
    @Value("${kai.service.websocket.signKey}")
    private String signKey;

    /**
     * 用户授权 key
     */
    public static final String AUTH_DATA_KEY = "AUTH_TOKEN";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(log.isDebugEnabled()){
            log.debug("websocket beforeHandshake ({}) connected. ip: {}", request.getURI(), request.getRemoteAddress());
        }
        //判断是否有连接参数，没有则直接返回false
        /*if(StringUtils.isEmpty(request.getURI().getQuery())){
            return false;
        }
        WebSocketAuthToken authToken;
        try {
            authToken = new WebSocketAuthToken.Builder(secretKey, signKey).fromQueryString(request.getURI().getQuery());
        }catch (IllegalArgumentException e){
            if(log.isDebugEnabled()){
                log.debug(String.format("websocket beforeHandshake (%s) connected. ip:%s had error" , request.getURI(), request.getRemoteAddress()), e);
            }
            return false;
        }
        if(authToken.isDirty()){
            //todo: 授权已久，则需要验证token是否有效
            //return false;

            //根据userId,deviceId验证token，验证不通过则返回false
            *//*if(!userServiceUtils.checkUserToken(userId,deviceId,token)){
                return false;
            }*//*

        }
        //session arrtibutes中存入userId信息
        attributes.put(AUTH_DATA_KEY, authToken);*/

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {
        if(log.isDebugEnabled()) {
            log.debug("afterHandshake ({}) connected. ip: {}",
                    request.getURI(), request.getRemoteAddress());
        }
    }
}
