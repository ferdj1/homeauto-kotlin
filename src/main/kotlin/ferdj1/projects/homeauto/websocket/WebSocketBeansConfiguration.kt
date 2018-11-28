package ferdj1.projects.homeauto.websocket

import ferdj1.projects.homeauto.model.ExecutedCommand
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import java.util.*

@Component
class WebSocketBeansConfiguration {
    @Bean
    fun sessionMap() = HashMap<String, WebSocketSession>()

    @Bean
    fun executedCommands() = LinkedList<ExecutedCommand>()


}