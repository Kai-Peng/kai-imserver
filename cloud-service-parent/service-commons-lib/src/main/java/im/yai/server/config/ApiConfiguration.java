package im.kai.server.config;

import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfiguration {

    //使用Feign自己的注解，使用springmvc的注解就会报错
    @Bean
    public Contract feignContract() {
        return new Contract.Default();
    }
}
