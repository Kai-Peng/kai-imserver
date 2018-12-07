package im.kai.server.api.vcode;

import feign.Param;
import feign.RequestLine;
import im.kai.server.config.ApiConfiguration;
import im.kai.server.domain.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;


/**
 * 验证码服务
 */
@Component
@FeignClient(value = "im-user-service" , configuration = ApiConfiguration.class)
public interface UserApiService {

    /**
     * 通过手机号码搜索用户用户信息
     * @return
     */
    @RequestLine("GET /v1/search/{keyword}")
    ApiResult searchUser(@Param("keyword") String keyword);

}
