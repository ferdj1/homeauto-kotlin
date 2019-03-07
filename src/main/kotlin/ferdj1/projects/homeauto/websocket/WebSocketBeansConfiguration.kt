package ferdj1.projects.homeauto.websocket

import ferdj1.projects.homeauto.model.ExecutedCommand
import ferdj1.projects.homeauto.model.ScheduledCommand
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import java.util.*
import kotlin.collections.HashMap

@Component
class WebSocketBeansConfiguration {
    @Bean
    fun sessionMap() = HashMap<String, WebSocketSession>()

    @Bean
    fun sessionHolder() = SessionHolder(null)

    @Bean
    fun executedCommands() = LinkedList<ExecutedCommand>()

    @Bean
    fun scheduledCommandToTimerTaskMap() = HashMap<ScheduledCommand, TimerTask>()

}