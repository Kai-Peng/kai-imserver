package im.kai.server.service.user.controller;


import im.kai.server.api.vcode.VerificationCodeService;
import im.kai.server.domain.ApiResult;
import im.kai.server.service.user.mapper.UserProfileMapper;
import im.kai.server.service.user.service.UserService;
import im.kai.server.service.user.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Controller
public class TestController {

    @Autowired
    private VerificationCodeService verificationCodeService ;

    @ResponseBody
    @RequestMapping(value = "/test/")
    public ApiResult test(HttpServletRequest req) throws IOException {

        return verificationCodeService.checkVerificationCode("18719398323" , "123456") ;
    }
}