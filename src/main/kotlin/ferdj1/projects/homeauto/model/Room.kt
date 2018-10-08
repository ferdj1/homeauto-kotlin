package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("room")
data class Room(@Id val id: Long,
                val name: String,
                val type: String)