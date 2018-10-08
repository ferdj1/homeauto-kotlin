package ferdj1.projects.homeauto.model

data class Subscription(val observedDeviceId: String,
                        val observedCommandId: String,
                        val observerList: List<Observer>)