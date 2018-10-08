package ferdj1.projects.homeauto.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("scene")
data class Scene(@Id val id: ObjectId = ObjectId(),
                 val name: String,
                 val commands: List<ExecutedCommand>)