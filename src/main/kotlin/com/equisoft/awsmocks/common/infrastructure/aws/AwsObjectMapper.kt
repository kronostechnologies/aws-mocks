package com.equisoft.awsmocks.common.infrastructure.aws

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy.UPPER_CAMEL_CASE
import java.io.InputStream

object AwsObjectMapper {
    private val objectMapper: ObjectMapper = ObjectMapper().setPropertyNamingStrategy(UPPER_CAMEL_CASE)

    fun <T> fromStream(stream: InputStream, valueType: Class<T>): T = objectMapper.readValue(stream, valueType)
}
