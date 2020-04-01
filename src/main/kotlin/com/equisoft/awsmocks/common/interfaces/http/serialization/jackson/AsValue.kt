package com.equisoft.awsmocks.common.interfaces.http.serialization.jackson

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@Retention
@JacksonAnnotationsInside
@JsonSerialize(using = AsValueSerializer::class)
annotation class AsValue
