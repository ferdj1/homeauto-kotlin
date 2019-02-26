package ferdj1.projects.homeauto.websocket

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ferdj1.projects.homeauto.model.DeviceDescription
import ferdj1.projects.homeauto.model.ExecutableCommand
import ferdj1.projects.homeauto.model.ExecutedCommand
import ferdj1.projects.homeauto.services.DeviceService
import ferdj1.projects.homeauto.services.ScheduledCommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.*
import kotlin.concurrent.schedule


/**
 * WebSocket Handler that handles communication between devices and the server(backend)
 */
@Component
class CommunicationHandler @Autowired constructor(@Autowired val backendToFrontendChangeHandler: BackendToFrontendChangeHandler,
                                                  @Autowired val scheduledCommandService: ScheduledCommandService,
                                                  @Autowired val deviceService: DeviceService,
                                                  @Autowired val sessionMap: HashMap<String, WebSocketSession>,
                                                  @Autowired val executedCommands: LinkedList<ExecutedCommand>) : TextWebSocketHandler() {
    // Message type constants
    private val DESCRIPTION_MESSAGE_TYPE = "description"
    private val CLIENT_TO_SERVER_EXECUTED_COMMAND_MESSAGE_TYPE = "clientToServerExecutedCommand"
    private val SERVER_TO_CLIENT_EXECUTED_COMMAND_MESSAGE_TYPE = "serverToClientExecutedCommand"
    private val EXECUTE_COMMAND_MESSAGE_TYPE = "executeCommand"


    // Timers
    private val timers = mutableListOf<TimerTask>()


    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionMap.forEach {
            if (it.value == session) {
                println("${it.key} disconnected.")
                sessionMap.remove(it.key)
            }
        }
        backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "connectionClosed", session.toString())
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val json = jacksonObjectMapper().readTree(message.payload)
        println(json)

        when (json["messageType"].asText()) {
            DESCRIPTION_MESSAGE_TYPE -> {
                val deviceId = json["id"].asText()
                sessionMap[deviceId] = session
                println("$deviceId connected.")

                if (!deviceService.existsById(deviceId)) {
                    val device = jacksonObjectMapper().readValue<DeviceDescription>(message.payload)
                    deviceService.add(device)
                } else {
                    val device = jacksonObjectMapper().readValue<DeviceDescription>(message.payload)
                    deviceService.update(device)
                }

                // Start running scheduled commands
                val scheduledCommands = scheduledCommandService.findAll()
                scheduledCommands.forEach {
                    val periodInMilliseconds: Long = when (it.intervalMetric) {
                        "seconds" -> it.interval * 1000L
                        "minutes" -> it.interval * 1000L * 60
                        "hours" -> it.interval * 1000L * 60 * 60
                        // Default 60 seconds
                        else -> 60 * 1000L
                    }

                    // TODO: Fix duplication bug when device restarts
                    val timerTask = Timer().schedule(0, periodInMilliseconds) {
                        val executableCommand = ExecutableCommand(EXECUTE_COMMAND_MESSAGE_TYPE, it.deviceId, it.commandId, it.parameters)
                        executeCommand(executableCommand)
                    }
                }
                backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "description", deviceId)

            }

            CLIENT_TO_SERVER_EXECUTED_COMMAND_MESSAGE_TYPE -> {
                val deviceId = json["deviceId"].asText()
                val commandId = json["commandId"].asText()
                val parameters = json["parameters"].map { it.asText() }
                val result = json["result"]?.asText()

                println("Device $deviceId executed $commandId with parameters $parameters and with result ${result
                        ?: "missing"}")

                val sync = Any()

                synchronized(sync) {
                    executedCommands.add(ExecutedCommand(null, deviceId, commandId, parameters, result))
                }

                // Check if frontend is asking for info command
                val getSetType = deviceService.findById(deviceId).get().commands.find { it.id == commandId }?.getSetType
                if(getSetType == "GET") {
                    backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "clientToServerExecutedInfoCommand", deviceId)
                } else {
                    backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "clientToServerExecutedCommand", deviceId)
                }
                // TODO Alert observers that some client executed a command
                // Idea: Let observers do whatever they need after the execution and then remove the executed command from the queue

                // For each observer do....
                // ....

                //synchronized(sync) {
                  //  executedCommands.poll()
                //}
            }
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("$session connected.")
        backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "sessionConnected", session.toString())
    }

    fun executeCommand(executableCommand: ExecutableCommand) {
        val deviceId = executableCommand.deviceId
        val executeCommandJSONString = jacksonObjectMapper().writeValueAsString(executableCommand)

        println("Executing command '${executableCommand.commandId}' on device '${executableCommand.deviceId}' with parameters: '${executableCommand.parameters}'")
        synchronized(sessionMap[deviceId] as Any) {
            sessionMap[deviceId]?.sendMessage(TextMessage(executeCommandJSONString))
        }
        backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "executeCommand", deviceId)
    }
}