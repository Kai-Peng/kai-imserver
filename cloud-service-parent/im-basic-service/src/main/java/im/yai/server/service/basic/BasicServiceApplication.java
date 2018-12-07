package im.kai.server.service.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"im.kai.server"})
public class BasicServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BasicServiceApplication.class, args);
    }
}

