package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.Id

data class Scene(@Id val id: Int,
                 val name: String,
                 val commands: List<ExecutedCommand>)