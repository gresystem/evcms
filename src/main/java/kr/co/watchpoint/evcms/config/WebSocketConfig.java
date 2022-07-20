package kr.co.watchpoint.evcms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import kr.co.watchpoint.evcms.interceptor.HandshakeInterceptor;
import kr.co.watchpoint.evcms.socket.handler.SocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

	@Autowired
	SocketHandler socketHandler;
	
    @Bean
    public HandshakeInterceptor handshakeInterceptor() {
        return new HandshakeInterceptor();
    }
    
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		//registry.addHandler(socketHandler, "/CP001");
		//registry.addHandler(socketHandler, "/webServices/ocpp/**").setAllowedOrigins("*").addInterceptors(new HandshakeInterceptor());
		registry.addHandler(socketHandler, "/webServices/ocpp/**").setAllowedOrigins("*").addInterceptors(handshakeInterceptor());
		//registry.addHandler(socketHandler, ".*").setAllowedOrigins("*").addInterceptors(new HandshakeInterceptor());
		//registry.addHandler(socketHandler, "*").setAllowedOrigins("*").withSockJS();

	}
}
