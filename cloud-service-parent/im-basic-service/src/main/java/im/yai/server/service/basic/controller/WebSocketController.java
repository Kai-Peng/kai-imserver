package im.kai.server.service.basic.controller;

import im.kai.server.constant.ApiFailCode;
import im.kai.server.domain.ApiResult;
import im.kai.server.domain.req.user.Device;
import im.kai.server.domain.websocket.WebSocketAuthToken;
import im.kai.server.exception.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/websocket")
public class WebSocketController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    private final String IM_MESSAGE_SERVICE_NAME = "IM-MESSAGE-SERVICE";

    /**
     * 数据加密密匙
     */
    @Value("${yai.service.websocket.secretKey}")
    private String secretKey;
    /**
     * 数据签名密匙
     */
    @Value("${yai.service.websocket.signKey}")
    private String signKey;

    /**
     * 获取websocket服务列表
     * @return
     */
    @GetMapping("/server")
    public ApiResult getServer(HttpServletRequest request)
    {
        ServiceInstance serviceInstance = loadBalancerClient.choose(IM_MESSAGE_SERVICE_NAME);
        if(serviceInstance == null){
            return ApiResult.fail(ApiFailCode.Message.EMPTY_MESSAGER_SERVER_LIST);
        }

        String scheme = serviceInstance.isSecure() ? "wss" : "ws";
        String host = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        int defaultPort = serviceInstance.isSecure() ? 443 : 80;
        String p = port == defaultPort ? "" : ":" + port;
        String uri = String.format("%s://%s%s/ws?%s", scheme, host, p, createAuthToken(request));

        return ApiResult.success(uri);
    }

    private String createAuthToken(HttpServletRequest request){
        String token = request.getHeader("X-TOKEN");
        String userId = request.getHeader("X-USER-ID");
        String device = request.getHeader("X-DEVICE");
        long timestamp = System.currentTimeMillis();

        WebSocketAuthToken.Builder builder = new WebSocketAuthToken.Builder(secretKey, signKey);
        WebSocketAuthToken authToken = new WebSocketAuthToken();
        authToken.setToken(token);
        authToken.setUserId(userId);
        authToken.setTimestamp(System.currentTimeMillis());
        authToken.setDevice(Device.Builder.build(device));

        //加密数据
        try {
            return builder.toQueryString(authToken);
        } catch (Exception e) {
            throw new ServerErrorException("生成websocket会话时出错", e);
        }
    }
}
