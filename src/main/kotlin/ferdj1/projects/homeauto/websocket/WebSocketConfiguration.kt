package ferdj1.projects.homeauto.websocket

import ferdj1.projects.homeauto.model.ExecutedCommand
import ferdj1.projects.homeauto.services.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import java.util.*

@Configuration @EnableWebSocket
class WebSocketConfiguration : WebSocketConfigurer {
    @Autowired
    private lateinit var deviceService: DeviceService

    @Autowired
    private lateinit var sessionMap: HashMap<String, WebSocketSession>

    @Autowired
    private lateinit var executedCommands: LinkedList<ExecutedCommand>

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(CommunicationHandler(deviceService, sessionMap, executedCommands), "/ws")
    }
}