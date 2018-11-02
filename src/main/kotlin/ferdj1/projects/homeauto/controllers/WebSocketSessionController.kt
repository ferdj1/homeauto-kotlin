package ferdj1.projects.homeauto.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.socket.WebSocketSession

@RestController
@RequestMapping("/api")
class WebSocketSessionController {
    @Autowired
    private lateinit var sessionMap: HashMap<String, WebSocketSession>

    @GetMapping("/websocketsessions")
    fun getWebSocketSessions() = sessionMap.mapValues { it.value.toString() }
}