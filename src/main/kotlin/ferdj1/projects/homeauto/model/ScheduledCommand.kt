package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.TypeAlias

@TypeAlias("scheduledCommand")
data class ScheduledCommand(val deviceId: String,
                            val commandId: String,
                            val parameters: List<String>,
                            val interval: Int)