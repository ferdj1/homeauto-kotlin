package ferdj1.projects.homeauto.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ferdj1.projects.homeauto.model.ExecutableCommand
import ferdj1.projects.homeauto.model.ExecutedCommand
import ferdj1.projects.homeauto.websocket.BackendToFrontendChangeHandler
import ferdj1.projects.homeauto.websocket.CommunicationHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class CommandExecutionController {
    @Autowired
    private lateinit var communicationHandler: CommunicationHandler

    @Autowired
    private lateinit var backendToFrontendChangeHandler: BackendToFrontendChangeHandler

    @PostMapping("/executeCommand")
    fun executeCommand(@RequestBody executableCommand: ExecutableCommand) {
        if (communicationHandler.sessionMap[executableCommand.deviceId] != null) {
            communicationHandler.executeCommand(executableCommand)
        }
    }
}