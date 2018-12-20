package im.kai.server.service.message.config;

import im.kai.server.service.message.controller.WSAuthenticatedInterceptor;
import im.kai.server.service.message.controller.WSMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    //@Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public WSMessageHandler wsMessageHandler(){
        return new WSMessageHandler();
    }

    @Bean
    public WSAuthenticatedInterceptor wsAuthenticatedInterceptor(){
        return new WSAuthenticatedInterceptor();
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        //container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(wsMessageHandler(), "/ws")
                .setAllowedOrigins("*")
                .addInterceptors(wsAuthenticatedInterceptor());
    }
}
