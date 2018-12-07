package im.kai.server.api.vcode;

import feign.Param;
import feign.RequestLine;
import im.kai.server.config.ApiConfiguration;
import im.kai.server.domain.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

//,configuration = FooConfiguration.class

/**
 * 验证码服务
 */
@Component
@FeignClient(value = "verification-code-service" , configuration = ApiConfiguration.class)
public interface VerificationCodeService {

    /**
     * 验证  手机验证码是否正确
     * @return
     */
    @RequestLine("PUT /v1/vcode/verify/{mobile}/{code}")
    ApiResult checkVerificationCode(@Param("mobile") String mobile, @Param("code") String code);

}
