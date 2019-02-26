package ferdj1.projects.homeauto.controllers

import ferdj1.projects.homeauto.model.DeviceDescription
import ferdj1.projects.homeauto.services.DeviceService
import ferdj1.projects.homeauto.websocket.BackendToFrontendChangeHandler
import ferdj1.projects.homeauto.websocket.NotificationStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class DevicesController {
    @Autowired
    private lateinit var deviceService: DeviceService

    @Autowired
    private lateinit var backendToFrontendChangeHandler: BackendToFrontendChangeHandler

    @GetMapping("/devices")
    fun getDevices() = deviceService.findAll()

    @GetMapping("/devices/{id}")
    fun getDevice(@PathVariable("id") id: String) = deviceService.findById(id)

    @PostMapping("/devices")
    fun addDevice(@RequestBody device: DeviceDescription): DeviceDescription {
        deviceService.add(device)
        backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "devicesChange", device.id)
        return device
    }

    @PostMapping("/devices/{id}")
    fun updateDevice(@PathVariable("id") id: String) {

    }
}