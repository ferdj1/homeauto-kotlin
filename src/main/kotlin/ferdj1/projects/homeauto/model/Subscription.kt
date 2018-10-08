package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("subscription")
data class Subscription(@Id val id: Long,
                        val observedDeviceId: String,
                        val observedCommandId: String,
                        val observerList: List<Observer>)

