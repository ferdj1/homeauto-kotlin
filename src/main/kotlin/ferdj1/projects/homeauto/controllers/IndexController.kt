package ferdj1.projects.homeauto.controllers

import ferdj1.projects.homeauto.services.DeviceService
import ferdj1.projects.homeauto.websocket.CommunicationHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.socket.WebSocketSession
import javax.annotation.Resource

@Controller
class IndexController {
    @Autowired
    private lateinit var deviceService: DeviceService

    @Resource
    lateinit var sessionMap: MutableMap<String, WebSocketSession>

    @GetMapping("/")
    fun getIndex(model: Model): String {
        model["devices"] = deviceService.findAll()

        // DBG
        println("FROM CONTROLLER: $sessionMap")

        model["sessions"] = sessionMap
        return "index"
    }
}