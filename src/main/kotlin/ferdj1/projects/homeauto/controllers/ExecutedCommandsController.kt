package ferdj1.projects.homeauto.controllers

import ferdj1.projects.homeauto.model.ExecutedCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.socket.WebSocketSession
import java.util.*

@RestController
@RequestMapping("/api")
class ExecutedCommandsController {
    @Autowired
    private lateinit var executedCommands: LinkedList<ExecutedCommand>

    @GetMapping("/executedCommands")
    fun getExecutedCommands() = executedCommands
}