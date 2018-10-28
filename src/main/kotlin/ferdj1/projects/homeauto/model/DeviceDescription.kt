package ferdj1.projects.homeauto.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("deviceDescription")
data class DeviceDescription(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        val messageType: String?,
        @Id val id: String,
        val name: String,
        val manufacturer: String,
        val type: String,
        val commands: List<Command>)