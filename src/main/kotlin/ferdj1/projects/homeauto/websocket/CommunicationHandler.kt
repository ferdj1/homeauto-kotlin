package ferdj1.projects.homeauto.websocket

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ferdj1.projects.homeauto.model.DeviceDescription
import ferdj1.projects.homeauto.model.ExecutableCommand
import ferdj1.projects.homeauto.model.ExecutedCommand
import ferdj1.projects.homeauto.services.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.*

@Component
class CommunicationHandler @Autowired constructor(@Autowired val deviceService: DeviceService,
                                                  @Autowired val sessionMap: HashMap<String, WebSocketSession>,
                                                  @Autowired val executedCommands: LinkedList<ExecutedCommand>) : TextWebSocketHandler() {
    // Message type constants
    private val DESCRIPTION_MESSAGE_TYPE = "description"
    private val CLIENT_TO_SERVER_EXECUTED_COMMAND_MESSAGE_TYPE = "clientToServerExecutedCommand"
    private val SERVER_TO_CLIENT_EXECUTED_COMMAND_MESSAGE_TYPE = "serverToClientExecutedCommand"
    private val EXECUTE_COMMAND_MESSAGE_TYPE = "executeCommand"


    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionMap.forEach {
            if (it.value == session) {
                println("${it.key} disconnected.")
                sessionMap.remove(it.key)
            }
        }
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
                }
            }

            CLIENT_TO_SERVER_EXECUTED_COMMAND_MESSAGE_TYPE -> {
                val deviceId = json["deviceId"].asText()
                val commandId = json["commandId"].asText()
                val parameters = json["parameters"].map { it.asText() }
                val result = json["result"]?.asText()

                println("Device $deviceId executed $commandId with parameters $parameters and with result ${result?: "missing"}")

                val sync = Any()

                synchronized(sync) {
                    executedCommands.add(ExecutedCommand(null, deviceId, commandId, parameters, result))
                }

                // TODO Alert observers that some client executed a command
                // Idea: Let observers do whatever they need after the execution and then remove the executed command from the queue

                // For each observer do....
                // ....

                //synchronized(sync) {
                    //executedCommands.poll()
                //}
            }
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("$session connected.")
    }

    fun executeCommand(executableCommand: ExecutableCommand) {
        val deviceId = executableCommand.deviceId
        val executeCommandJSONString = jacksonObjectMapper().writeValueAsString(executableCommand)

        println("Executing command '${executableCommand.commandId}' on device '${executableCommand.deviceId}' with parameters: '${executableCommand.parameters}'")
        synchronized(sessionMap[deviceId] as Any) {
            sessionMap[deviceId]?.sendMessage(TextMessage(executeCommandJSONString))
        }
    }
}