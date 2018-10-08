package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("command")
data class Command(@Id val id: String,
                   val name: String,
                   val description: String,
                   val parameterTypes: List<String>,
                   val returnType: String)