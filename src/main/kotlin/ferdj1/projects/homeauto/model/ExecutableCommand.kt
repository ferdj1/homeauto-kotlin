package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.TypeAlias

@TypeAlias("executableCommand")
data class ExecutableCommand(val messageType: String,
                             val deviceId: String,
                             val commandId: String,
                             val parameters: List<String?>?)
