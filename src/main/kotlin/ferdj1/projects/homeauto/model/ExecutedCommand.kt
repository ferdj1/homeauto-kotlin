package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.TypeAlias

@TypeAlias("executedCommand")
data class ExecutedCommand(val deviceId: String,
                           val commandId: String,
                           val parameters: List<String>)
