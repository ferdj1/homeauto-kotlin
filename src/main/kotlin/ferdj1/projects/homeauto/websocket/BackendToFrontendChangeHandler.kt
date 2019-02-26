package ferdj1.projects.homeauto.websocket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import javax.management.AttributeChangeNotification

/**
 *  WebSocket Handler that notifies frontend when there is a change on backend side
 */
@Component
class BackendToFrontendChangeHandler @Autowired constructor(@Autowired val sessionHolder: SessionHolder): TextWebSocketHandler() {
    private object SyncKey

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        println("Lost connection to frontend websocket, status: $status")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("Frontend notifier created.")
        sessionHolder.session = session
    }


    fun notifyFrontend(status: NotificationStatus, type: String?, info: String?) {
        synchronized(SyncKey) {
            //language=JSON
            sessionHolder.session?.sendMessage(TextMessage("{\n  \"status\": \"$status\",\n  \"type\": \"${type?:""}\",\n  \"info\": \"${info?:""}\"\n}"))
        }
    }
}