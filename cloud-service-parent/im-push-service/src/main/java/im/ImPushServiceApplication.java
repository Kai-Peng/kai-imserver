package im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"im.kai.server"})
@EnableFeignClients(basePackages = {"im.kai.server"})
public class ImPushServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImPushServiceApplication.class, args);
    }
}
