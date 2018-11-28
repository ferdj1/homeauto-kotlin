package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.TypeAlias

@TypeAlias("executedCommand")
data class ExecutedCommand(val messageType: String?,
                           val deviceId: String,
                           val commandId: String,
                           val parameters: List<String?>?,
                           val result: String?)
