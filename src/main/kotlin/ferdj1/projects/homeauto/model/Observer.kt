package ferdj1.projects.homeauto.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

@TypeAlias("observer")
data class Observer(@Id @JsonSerialize(using = ToStringSerializer::class) val id: ObjectId,
                    val observerDeviceId: String,
                    val observerCommandId: String,
                    val parameters: List<Parameter?>?,
                    val parameterComparators: List<ParameterComparator?>?)