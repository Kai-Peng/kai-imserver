package im.kai.server.service.user.config;

import im.kai.server.service.user.filter.BasicAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private BasicAuthenticationFilter basicAuthenticationFilter ;

    @Bean
    public BasicAuthenticationFilter basicAuthenticationFilter() {

        return new BasicAuthenticationFilter() ;

    }

    /**
     * 注册 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(basicAuthenticationFilter);
        //registry.addInterceptor(rbac_filter);
    }


}
