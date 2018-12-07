package im.kai.server.service.verificationcode;

import im.kai.server.service.verificationcode.mq.VCodeMessager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"im.kai.server"})
@EnableBinding(VCodeMessager.class)
public class VerificationCodeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(VerificationCodeServiceApplication.class, args);
    }
}

