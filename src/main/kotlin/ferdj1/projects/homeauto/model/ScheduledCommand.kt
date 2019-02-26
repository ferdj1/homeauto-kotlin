package ferdj1.projects.homeauto.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("scheduledCommand")
data class ScheduledCommand(@Id val id: ObjectId,
                            val deviceId: String,
                            val commandId: String,
                            val parameters: List<String>,
                            val interval: Int,
                            val intervalMetric: String)