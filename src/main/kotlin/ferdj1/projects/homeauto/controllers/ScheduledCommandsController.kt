package ferdj1.projects.homeauto.controllers

import ferdj1.projects.homeauto.model.ExecutableCommand
import ferdj1.projects.homeauto.model.ScheduledCommand
import ferdj1.projects.homeauto.services.ScheduledCommandService
import ferdj1.projects.homeauto.websocket.BackendToFrontendChangeHandler
import ferdj1.projects.homeauto.websocket.CommunicationHandler
import ferdj1.projects.homeauto.websocket.NotificationStatus
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule


@RestController
@RequestMapping("/api")
class ScheduledCommandsController {
    private val EXECUTE_COMMAND_MESSAGE_TYPE = "executeCommand"

    @Autowired
    private lateinit var scheduledCommandService: ScheduledCommandService

    @Autowired
    private lateinit var backendToFrontendChangeHandler: BackendToFrontendChangeHandler

    @Autowired
    private lateinit var communicationHandler: CommunicationHandler

    @Autowired
    private lateinit var scheduledCommandToTimerTaskMap: HashMap<ScheduledCommand, TimerTask>

    @GetMapping("/scheduledCommands")
    fun getScheduledCommands() = scheduledCommandService.findAll()

    @PostMapping("/scheduledCommands")
    fun scheduleCommand(@RequestBody command: ScheduledCommand): ScheduledCommand {
        val uniqueIdCommand = command.copy(id = ObjectId())

        // Start running scheduled command

        val periodInMilliseconds: Long = when (uniqueIdCommand.intervalMetric) {
            "seconds" -> uniqueIdCommand.interval * 1000L
            "minutes" -> uniqueIdCommand.interval * 1000L * 60
            "hours" -> uniqueIdCommand.interval * 1000L * 60 * 60
            // Default 60 seconds
            else -> 60 * 1000L
        }

        Timer().schedule(periodInMilliseconds, periodInMilliseconds) {
            val executableCommand = ExecutableCommand(EXECUTE_COMMAND_MESSAGE_TYPE,
                    uniqueIdCommand.deviceId,
                    uniqueIdCommand.commandId,
                    uniqueIdCommand.parameters)
            communicationHandler.executeCommand(executableCommand)
        }.also { timerTask ->
            if (scheduledCommandToTimerTaskMap.containsKey(uniqueIdCommand)) {
                timerTask.cancel()
            } else {
                scheduledCommandToTimerTaskMap[uniqueIdCommand] = timerTask
            }
        }



        backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "schedulerChange", command.commandId)

        return scheduledCommandService.scheduleCommand(uniqueIdCommand)
    }

    @DeleteMapping("/scheduledCommands")
    fun deleteScheduledCommand(@RequestBody scheduledCommand: ScheduledCommand) {
        backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "schedulerChange", "no info")

        scheduledCommandToTimerTaskMap[scheduledCommand]?.cancel()
        scheduledCommandToTimerTaskMap.remove(scheduledCommand)

        scheduledCommandService.delete(scheduledCommand)
    }
}