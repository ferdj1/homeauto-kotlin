package ferdj1.projects.homeauto.websocket

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class SessionConfiguration {
    @Bean
    fun sessionMap() = HashMap<String, WebSocketSession>()
}