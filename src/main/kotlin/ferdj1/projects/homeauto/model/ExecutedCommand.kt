package ferdj1.projects.homeauto.model

data class ExecutedCommand(val deviceId: String,
                           val commandId: String,
                           val parameters: List<String>)
