package ferdj1.projects.homeauto.controllers

import ferdj1.projects.homeauto.model.ScheduledCommand
import ferdj1.projects.homeauto.services.ScheduledCommandService
import ferdj1.projects.homeauto.websocket.BackendToFrontendChangeHandler
import ferdj1.projects.homeauto.websocket.NotificationStatus
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api")
class ScheduledCommandsController {
    @Autowired
    private lateinit var scheduledCommandService: ScheduledCommandService

    @Autowired
    private lateinit var backendToFrontendChangeHandler: BackendToFrontendChangeHandler

    @GetMapping("/scheduledCommands")
    fun getScheduledCommands() = scheduledCommandService.findAll()

    @PostMapping("/scheduledCommands")
    fun scheduleCommand(@RequestBody command: ScheduledCommand): ScheduledCommand {
        val uniqueIdCommand = command.copy(id = ObjectId())
        // TODO: Spawn thread for scheduled command

        backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "schedulerChange", command.commandId)

        return scheduledCommandService.scheduleCommand(uniqueIdCommand)
    }

    @DeleteMapping("/scheduledCommands")
    fun deleteDeviceFromRoom(@RequestBody scheduledCommand: ScheduledCommand) {
        backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "schedulerChange", "no info")
        scheduledCommandService.delete(scheduledCommand)
    }
}