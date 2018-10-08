package ferdj1.projects.homeauto.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("observer")
data class Observer(@Id val id: ObjectId,
                    val observerDeviceId: String,
                    val executedCommandId: String,
                    val useOption: Boolean,
                    val optionType: String,
                    val sign: String,
                    val comparedValue: String)