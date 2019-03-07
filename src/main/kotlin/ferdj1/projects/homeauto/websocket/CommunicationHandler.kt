package ferdj1.projects.homeauto.websocket

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ferdj1.projects.homeauto.model.DeviceDescription
import ferdj1.projects.homeauto.model.ExecutableCommand
import ferdj1.projects.homeauto.model.ExecutedCommand
import ferdj1.projects.homeauto.model.ScheduledCommand
import ferdj1.projects.homeauto.services.DeviceService
import ferdj1.projects.homeauto.services.ScheduledCommandService
import ferdj1.projects.homeauto.services.SubscriptionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule


/**
 * WebSocket Handler that handles communication between devices and the server(backend)
 */
@Component
class CommunicationHandler @Autowired constructor(@Autowired val backendToFrontendChangeHandler: BackendToFrontendChangeHandler,
                                                  @Autowired val scheduledCommandService: ScheduledCommandService,
                                                  @Autowired val subscriptionService: SubscriptionService,
                                                  @Autowired val deviceService: DeviceService,
                                                  @Autowired val sessionMap: HashMap<String, WebSocketSession>,
                                                  @Autowired val executedCommands: LinkedList<ExecutedCommand>,
                                                  @Autowired val scheduledCommandToTimerTaskMap: HashMap<ScheduledCommand, TimerTask>) : TextWebSocketHandler() {
    // Message type constants
    private val DESCRIPTION_MESSAGE_TYPE = "description"
    private val CLIENT_TO_SERVER_EXECUTED_COMMAND_MESSAGE_TYPE = "clientToServerExecutedCommand"
    private val SERVER_TO_CLIENT_EXECUTED_COMMAND_MESSAGE_TYPE = "serverToClientExecutedCommand"
    private val EXECUTE_COMMAND_MESSAGE_TYPE = "executeCommand"

    // Limits
    private val EXECUTED_COMMANDS_LIMIT = 1000

    private val STRING_DATATYPE = "string"
    private val NUMBER_DATATYPE = "number"
    private val INTEGER_DATATYPE = "integer"
    private val BOOLEAN_DATATYPE = "boolean"
    private val NULL_DATATYPE = "null"

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        var disconnectedDeviceId: String? = null

        // Remove device session
        sessionMap.forEach {
            if (it.value == session) {
                disconnectedDeviceId = it.key
                return@forEach
            }
        }

        println("$disconnectedDeviceId disconnected.")
        sessionMap.remove(disconnectedDeviceId)

        // Remove timers associated with the device
        scheduledCommandToTimerTaskMap.filter { it.key.deviceId == disconnectedDeviceId }.forEach {
            it.value.cancel()
        }
        val scheduledCommandToTimerTaskMapCopy = HashMap<ScheduledCommand, TimerTask>()
                .also { it.putAll(scheduledCommandToTimerTaskMap) }
        scheduledCommandToTimerTaskMap.clear()
        scheduledCommandToTimerTaskMap.putAll(scheduledCommandToTimerTaskMapCopy.filter { it.key.deviceId != disconnectedDeviceId })

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
                scheduledCommands.filter { it.deviceId == deviceId }.forEach {
                    val periodInMilliseconds: Long = when (it.intervalMetric) {
                        "seconds" -> it.interval * 1000L
                        "minutes" -> it.interval * 1000L * 60
                        "hours" -> it.interval * 1000L * 60 * 60
                        // Default 60 seconds
                        else -> 60 * 1000L
                    }

                    Timer().schedule(periodInMilliseconds, periodInMilliseconds) {
                        val executableCommand = ExecutableCommand(EXECUTE_COMMAND_MESSAGE_TYPE, it.deviceId, it.commandId, it.parameters)
                        executeCommand(executableCommand)
                    }.also { timerTask ->
                        if (scheduledCommandToTimerTaskMap.containsKey(it)) {
                            timerTask.cancel()
                        } else {
                            scheduledCommandToTimerTaskMap[it] = timerTask
                        }
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
                if (getSetType == "GET") {
                    backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "clientToServerExecutedInfoCommand", deviceId)
                } else {
                    backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "clientToServerExecutedCommand", deviceId)
                }

                val observers = subscriptionService
                        .findAll()
                        .find { it.observedDeviceId == deviceId && it.observedCommandId == commandId }?.observerList

                observers?.forEach {
                    // Parameters for the observer command
                    val params = it.parameters?.map { parameter ->
                        if (parameter!!.usesObservedValue) {
                            if (parameter.observedParameterIndex == -1) {
                                result
                            } else {
                                parameters[parameter.observedParameterIndex!!]
                            }
                        } else {
                            parameter.value
                        }
                    }

                    // Conditional
                    val parameterComparators = it.parameterComparators
                    val isExecutable = parameterComparators?.filter { pc -> pc?.useOption == true }?.mapIndexed { index, pc ->
                        when (pc?.sign) {
                            "EQ" -> {
                                when (pc.optionType) {
                                    INTEGER_DATATYPE -> return@mapIndexed parameters?.get(index)?.toInt() == (pc.comparedValue?.toInt())
                                    NUMBER_DATATYPE -> return@mapIndexed parameters?.get(index)?.toDouble() == (pc.comparedValue?.toDouble())
                                    STRING_DATATYPE -> return@mapIndexed parameters?.get(index) == (pc.comparedValue)
                                    BOOLEAN_DATATYPE -> return@mapIndexed parameters?.get(index)?.toBoolean() == (pc.comparedValue?.toBoolean())
                                    else -> return@mapIndexed false
                                }
                            }

                            "NE" -> {
                                when (pc.optionType) {
                                    INTEGER_DATATYPE -> return@mapIndexed parameters?.get(index)?.toInt() != (pc.comparedValue?.toInt())
                                    NUMBER_DATATYPE -> return@mapIndexed parameters?.get(index)?.toDouble() != (pc.comparedValue?.toDouble())
                                    STRING_DATATYPE -> return@mapIndexed parameters?.get(index) != (pc.comparedValue)
                                    BOOLEAN_DATATYPE -> return@mapIndexed parameters?.get(index)?.toBoolean() != (pc.comparedValue?.toBoolean())
                                    else -> return@mapIndexed false
                                }
                            }

                            "LT" -> {
                                when (pc.optionType) {
                                    INTEGER_DATATYPE -> return@mapIndexed parameters?.get(index)?.toInt()!! < (pc.comparedValue?.toInt()!!)
                                    NUMBER_DATATYPE -> return@mapIndexed parameters?.get(index)?.toDouble()!! < (pc.comparedValue?.toDouble()!!)
                                    else -> return@mapIndexed false
                                }
                            }

                            "LE" -> {
                                when (pc.optionType) {
                                    INTEGER_DATATYPE -> return@mapIndexed parameters?.get(index)?.toInt()!! <= (pc.comparedValue?.toInt()!!)
                                    NUMBER_DATATYPE -> return@mapIndexed parameters?.get(index)?.toDouble()!! <= (pc.comparedValue?.toDouble()!!)
                                    else -> return@mapIndexed false
                                }
                            }

                            "GT" -> {
                                when (pc.optionType) {
                                    INTEGER_DATATYPE -> return@mapIndexed parameters?.get(index)?.toInt()!! > (pc.comparedValue?.toInt()!!)
                                    NUMBER_DATATYPE -> return@mapIndexed parameters?.get(index)?.toDouble()!! > (pc.comparedValue?.toDouble()!!)
                                    else -> return@mapIndexed false
                                }
                            }

                            "GE" -> {
                                when (pc.optionType) {
                                    INTEGER_DATATYPE -> return@mapIndexed parameters?.get(index)?.toInt()!! >= (pc.comparedValue?.toInt()!!)
                                    NUMBER_DATATYPE -> return@mapIndexed parameters?.get(index)?.toDouble()!! >= (pc.comparedValue?.toDouble()!!)
                                    else -> return@mapIndexed false
                                }
                            }

                            else -> return@mapIndexed false
                        }
                    }?.all { b -> b == true }

                    println("IS EXECUTABLE: $isExecutable")

                    if(isExecutable === null) {
                        return@forEach
                    }

                    if (isExecutable) {
                        val executableCommand = ExecutableCommand(EXECUTE_COMMAND_MESSAGE_TYPE,
                                it.observerDeviceId,
                                it.observerCommandId,
                                params)
                        executeCommand(executableCommand)
                    }
                }

                synchronized(sync) {
                    if (executedCommands.size >= EXECUTED_COMMANDS_LIMIT) {
                        executedCommands.clear()
                    }
                }
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
        if (sessionMap[deviceId] !== null) {
            synchronized(sessionMap[deviceId] as Any) {
                sessionMap[deviceId]?.sendMessage(TextMessage(executeCommandJSONString))
            }
            backendToFrontendChangeHandler.notifyFrontend(NotificationStatus.OK, "executeCommand", deviceId)
        }
    }
}