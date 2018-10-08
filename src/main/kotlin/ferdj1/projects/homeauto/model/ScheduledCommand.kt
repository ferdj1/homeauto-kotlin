package ferdj1.projects.homeauto.model

data class ScheduledCommand(val deviceId: String,
                            val commandId: String,
                            val parameters: List<String>,
                            val interval: Int)