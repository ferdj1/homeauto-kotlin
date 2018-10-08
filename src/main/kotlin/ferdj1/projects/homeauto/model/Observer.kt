package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.TypeAlias

@TypeAlias("observer")
data class Observer(val observerDeviceId: String,
                    val executedCommandId: String,
                    val useOption: Boolean,
                    val optionType: String,
                    val sign: String,
                    val comparedValue: String)