package im.kai.server.service.verificationcode.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 开发环境用的生成"月日小时"的验证码生成器
 */
@Profile("dev")
@Service
public class DateVCodeGenerator implements VCodeGenerator {
    SimpleDateFormat format = new SimpleDateFormat("MMddHH");
    @Override
    public String generate() {
        return format.format(new Date());
    }
}
