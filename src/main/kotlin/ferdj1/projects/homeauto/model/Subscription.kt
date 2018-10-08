package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.TypeAlias

@TypeAlias("subscription")
data class Subscription(val observedDeviceId: String,
                        val observedCommandId: String,
                        val observerList: List<Observer>)