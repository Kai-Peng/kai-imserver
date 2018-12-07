package im.kai.server.service.verificationcode.controller;

import im.kai.server.domain.ApiResult;
import im.kai.server.constant.ApiFailCode;
import im.kai.server.service.verificationcode.bean.VCodeCreatedMessage;
import im.kai.server.service.verificationcode.service.VCodeGenerator;
import im.kai.server.service.verificationcode.utils.RedisUtils;
import im.kai.server.utils.RandomUitls;
import im.kai.server.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/vcode")
@Slf4j
@RefreshScope
public class VCodeController {

    @Autowired
    RedisUtils redisUtils;

    @Resource(name = "vCodeProducter")
    MessageChannel vCodeProducter;

    @Resource
    VCodeGenerator vCodeGenerator;
    /**
     * 验证码的失效时间：单位（秒）
     */
    @Value("${setting.cache.verificationcode.timeout:600}")
    private int codeTimeoutSeconds;


    /**
     * 生成新的验证码，并向手机发送短信消息
     * @param mobile 手机号码
     * @return
     */
    @PutMapping("/create/{mobile}")
    public ApiResult create(@PathVariable("mobile") String mobile){
        if(!Utils.isValidMobileNumber(mobile)){
            return ApiResult.fail(ApiFailCode.INVALID_MOBILE_NUMBER);
        }

        String code = vCodeGenerator.generate();
        redisUtils.getOps().set(redisUtils.getVCodeCacheKey(mobile), code, codeTimeoutSeconds, TimeUnit.SECONDS);

        VCodeCreatedMessage message = new VCodeCreatedMessage(mobile, code, VCodeCreatedMessage.Mode.SMS);
        boolean r = vCodeProducter.send(MessageBuilder.withPayload(message).build());
        log.debug("vCodeProducter.send={}", r);
        return ApiResult.success();
    }

    /**
     * 验证某个手机的验证码是否正确
     * @param mobile 手机号码
     * @param code 需要验证的验证码
     * @return
     */
    @PutMapping("/verify/{mobile}/{code}")
    public ApiResult verify(
            @PathVariable("mobile") String mobile,
            @PathVariable("code") String code){

        if(!Utils.isValidMobileNumber(mobile)){
            return ApiResult.fail(ApiFailCode.INVALID_MOBILE_NUMBER);
        }

        String v = redisUtils.getOps().get(redisUtils.getVCodeCacheKey(mobile));
        if(v == null || !v.equals(code)){
            return ApiResult.fail(ApiFailCode.VCode.INVALID_CODE);
        }
        return ApiResult.success();
    }


    /**
     * 获取某个手机当前对应的验证码
     * @param mobile 手机号码
     * @return
     */
    /**
    @GetMapping("/get/{mobile}")
    public ApiResult get(@PathVariable("mobile") String mobile){
        return ApiResult.success(redisUtils.getOps().get(redisUtils.getVCodeCacheKey(mobile)));
    }
    **/
}
