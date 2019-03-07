package ferdj1.projects.homeauto.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("subscription")
data class Subscription(@Id @JsonSerialize(using = ToStringSerializer::class) val id: ObjectId,
                        val observedDeviceId: String,
                        val observedCommandId: String,
                        val observerList: MutableList<Observer>)

