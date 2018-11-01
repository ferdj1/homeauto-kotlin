package ferdj1.projects.homeauto.controllers

import ferdj1.projects.homeauto.services.ScheduledCommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class ScheduledCommandsController {
    @Autowired
    private lateinit var scheduledCommandService: ScheduledCommandService

    @GetMapping("/scheduledCommands")
    fun getScheduledCommands() = scheduledCommandService.findAll()
}