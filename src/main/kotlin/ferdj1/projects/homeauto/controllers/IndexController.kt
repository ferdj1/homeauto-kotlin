package ferdj1.projects.homeauto.controllers

import ferdj1.projects.homeauto.services.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController {
    @Autowired
    private lateinit var deviceService: DeviceService

    @GetMapping("/")
    fun getIndex(model: Model): String {
        model["devices"] = deviceService.findAll()
        return "index"
    }
}