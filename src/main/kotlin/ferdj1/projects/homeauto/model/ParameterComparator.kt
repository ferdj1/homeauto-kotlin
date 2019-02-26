package ferdj1.projects.homeauto.model

import org.springframework.data.annotation.TypeAlias

@TypeAlias("parameterComparator")
data class ParameterComparator(val useOption: Boolean,
                               val optionType: String?,
                               val sign: String?,
                               val comparedValue: String?)