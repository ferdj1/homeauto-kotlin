package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("deviceDescription")
data class DeviceDescription(@Id val id: String,
                             val name: String,
                             val manufacturer: String,
                             val type: String,
                             val commands: List<Command>)