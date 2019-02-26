package ferdj1.projects.homeauto.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("observer")
data class Observer(@Id val id: ObjectId,
                    val observerDeviceId: String,
                    val observerCommandId: String,
                    val parameters: List<Parameter?>?,
                    val parameterComparators: List<ParameterComparator?>?)