package ferdj1.projects.homeauto.websocket

import org.springframework.web.socket.WebSocketSession


/**
 *  Used to setup frontend websocket session in websocket configuration.
 *  This avoids the problems with lateinit var session not being initialized.
 */
data class SessionHolder(var session: WebSocketSession?)