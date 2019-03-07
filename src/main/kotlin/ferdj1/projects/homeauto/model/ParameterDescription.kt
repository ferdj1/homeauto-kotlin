package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.TypeAlias

@TypeAlias("parameterDescription")
data class ParameterDescription(val name: String,
                                val type: String?,
                                val specialType: String?,
                                val limitType: String,
                                val min: String?,
                                val max: String?,
                                val values: List<String?>?,
                                val defaultValue: String?)