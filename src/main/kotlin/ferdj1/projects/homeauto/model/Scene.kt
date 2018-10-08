package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("scene")
data class Scene(@Id val id: Long,
                 val name: String,
                 val commands: List<ExecutedCommand>)