package im.kai.server.service.gateway.filter;

import im.kai.server.domain.req.user.Device;
import im.kai.server.utils.TokenCacheUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;
/**
 * X-TOKEN 头转为 X-USER-ID头
 **/
@Slf4j
@Component
public class XTokenFilter implements GlobalFilter, Ordered {
    private final String X_TOKEN_HEADER = "X-TOKEN";
    private final String X_DEVICE_HEADER = "X-DEVICE";
    private final String X_USER_ID_HEADER = "X-USER-ID";

    @Autowired
    private Config xconfig;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        log.trace("XTokenToAuthTokenFilter", route.toString());
        ServerHttpRequest request = exchange.getRequest();

        HttpHeaders headers = request.getHeaders();
        //客户端如果带此HTTP头则请求属于非法
        if(headers.containsKey(X_USER_ID_HEADER)){
            return end(exchange.getResponse());
        }

        if(!headers.containsKey(X_DEVICE_HEADER)){
            return end(exchange.getResponse());
        }
        try {
            Device device = Device.Builder.build(headers.getFirst(X_DEVICE_HEADER));
        }catch (Exception ex) {
            //X-DEVICE参数非法
            return end(exchange.getResponse());
        }

        if(!headers.containsKey(X_TOKEN_HEADER)){
            //检查是不是不需要验证登录的路由
            if(!xconfig.isExcludeRouter(route.getId())){
                return end(exchange.getResponse());
            }
        }else{
            String userId = getTokeUserId(headers.get(X_TOKEN_HEADER).get(0));
            //todo: 测试期，弄一个默认值
            if(StringUtils.isEmpty(userId)) userId = "1";
            request = request.mutate().header(X_USER_ID_HEADER, userId).build();
        }
        return chain.filter(exchange.mutate().request(request).build());
    }

    private String getTokeUserId(String token){
        return redisTemplate.opsForValue().get(TokenCacheUtils.getKey(token));
    }

    private Mono<Void> end(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 需要排除验证的地址
     */
    @Data
    @Component
    @ConfigurationProperties("im.filter.xtoken")
    public static class Config{
        private Set<String> excludeRouterId;

        public boolean isExcludeRouter(String id){
            return this.excludeRouterId != null && excludeRouterId.contains(id);
        }
    }
}
