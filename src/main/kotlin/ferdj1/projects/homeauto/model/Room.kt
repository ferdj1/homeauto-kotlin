package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.Id

data class Room(@Id val id: Int,
                val name: String,
                val type: String)