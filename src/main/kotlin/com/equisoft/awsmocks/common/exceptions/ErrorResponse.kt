package com.equisoft.awsmocks.common.exceptions

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "__type")
sealed class Error(val code: String, val message: String = "")

@JsonTypeName("NotFoundException")
class NotFoundError(code: String? = null) : Error(code ?: "NotFound")

@JsonTypeName("ResourceNotFoundException")
object ResourceNotFoundError : Error("ResourceNotFoundException")

class ErrorResponse(val error: Error)
