package ferdj1.projects.homeauto.controllers

import ferdj1.projects.homeauto.services.RoomService
import ferdj1.projects.homeauto.websocket.CommunicationHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class RoomController {
    @Autowired
    private lateinit var roomService: RoomService

    @GetMapping("/rooms")
    fun getRooms(model: Model): String {
        model["rooms"] = roomService.findAll()
        return "rooms"
    }
}