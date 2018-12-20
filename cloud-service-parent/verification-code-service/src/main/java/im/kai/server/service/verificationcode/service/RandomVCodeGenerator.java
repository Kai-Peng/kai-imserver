package im.kai.server.service.verificationcode.service;

import im.kai.server.utils.RandomUitls;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生成随机验证码生成器
 */
@Profile("prod")
@Service
public class RandomVCodeGenerator implements VCodeGenerator {
    @Override
    public String generate() {
        return RandomUitls.generate6RndNumber();
    }
}
