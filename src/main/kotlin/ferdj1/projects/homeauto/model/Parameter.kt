package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.TypeAlias

@TypeAlias("parameter")
data class Parameter(val usesObservedValue: Boolean,
                     val value: String?,
                     val observedParameterIndex: Int?)
