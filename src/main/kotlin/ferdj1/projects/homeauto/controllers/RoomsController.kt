package ferdj1.projects.homeauto.controllers

import ferdj1.projects.homeauto.model.Room
import ferdj1.projects.homeauto.services.DeviceService
import ferdj1.projects.homeauto.services.RoomService
import ferdj1.projects.homeauto.websocket.BackendToFrontendChangeHandler
import ferdj1.projects.homeauto.websocket.NotificationStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class RoomsController {
    @Autowired
    private lateinit var roomService: RoomService

    @Autowired
    private lateinit var deviceService: DeviceService

    @Autowired
    private lateinit var backendToFrontendChangeHandler: BackendToFrontendChangeHandler

    @GetMapping("/rooms")
    fun getRooms(model: Model) = roomService.findAll()

    @PostMapping("/rooms")
    fun addRoom(@RequestBody room: Room): Room {
        roomService.add(room)
        backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "roomsChange", room.name)
        return room
    }

    @PostMapping("/rooms/{roomName}/{deviceId}")
    fun addDeviceToRoom(@PathVariable(value = "roomName") roomName: String,
                        @PathVariable(value = "deviceId") deviceId: String): Room? {
        val room = roomService.findByName(roomName)
        val device = deviceService.findById(deviceId).get()
        //Remove this !! asap
        if (!room?.devices?.contains(device)!!) {
            room.devices.add(device)
        }

        roomService.update(room)
        backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "roomsChange", deviceId)
        return roomService.findByName(roomName)
    }

    @DeleteMapping("/rooms/{name}")
    fun deleteRoom(@PathVariable(value = "name") name: String) {
        backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "roomsChange", name)
        roomService.deleteByName(name)
    }

    @DeleteMapping("/rooms/{roomName}/{deviceId}")
    fun deleteDeviceFromRoom(@PathVariable(value = "roomName") roomName: String,
                             @PathVariable(value = "deviceId") deviceId: String) {
        val room = roomService.findByName(roomName)
        val device = deviceService.findById(deviceId).get()
        //Remove this !! asap
        if (room?.devices?.contains(device)!!) {
            room.devices.remove(device)
        }
        backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "roomsChange", device.id)
        roomService.update(room)
    }
}