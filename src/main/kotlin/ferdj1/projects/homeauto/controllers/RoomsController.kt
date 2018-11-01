package ferdj1.projects.homeauto.controllers

import ferdj1.projects.homeauto.model.Room
import ferdj1.projects.homeauto.services.RoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class RoomsController {
    @Autowired
    private lateinit var roomService: RoomService

    @GetMapping("/rooms")
    fun getRooms(model: Model) = roomService.findAll()

    @PostMapping("/rooms")
    fun addRoom(@RequestBody room: Room): Room {
        roomService.add(room)
        return room
    }

    @DeleteMapping("/rooms/{name}")
    fun deleteRoom(@PathVariable(value="name") name: String) = roomService.deleteByName(name)
}