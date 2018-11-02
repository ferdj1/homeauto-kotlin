package ferdj1.projects.homeauto.websocket

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ferdj1.projects.homeauto.model.DeviceDescription
import ferdj1.projects.homeauto.services.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import javax.annotation.Resource

@Component
class CommunicationHandler @Autowired constructor(@Autowired val deviceService: DeviceService,
                                                  @Autowired val sessionMap: HashMap<String, WebSocketSession>) : TextWebSocketHandler() {
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

                // DBG
                println("FROM HANDLER: $sessionMap")

                println("$deviceId connected.")

                if (!deviceService.existsById(deviceId)) {
                    val device = jacksonObjectMapper().readValue<DeviceDescription>(message.payload)

                    //DBG
                    println(device)

                    deviceService.add(device)
                }
            }

            CLIENT_TO_SERVER_EXECUTED_COMMAND_MESSAGE_TYPE -> {
                val deviceId = json["deviceId"].asText()
                val commandId = json["commandId"].asText()
                val parameters = json["result"].asText()

                // TODO Alert observers that some client executed a command
            }
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("$session connected.")
    }
}