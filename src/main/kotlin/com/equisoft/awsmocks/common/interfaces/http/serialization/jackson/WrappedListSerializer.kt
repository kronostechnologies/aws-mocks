package com.equisoft.awsmocks.common.interfaces.http.serialization.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

/**
 * This class is used to bypass Jackson limitation with XmlElementWrapper and XmlElements.
 * Jackson complains multiple properties are named "<fieldName>" although there's a wrapper around it.
 */
open class WrappedListSerializer(private val fieldName: String) : JsonSerializer<List<Any>>() {
    override fun serialize(values: List<Any>, jgen: JsonGenerator, provider: SerializerProvider) {
        if (values.isNotEmpty()) {
            jgen.writeStartArray()
            jgen.writeStartObject()
            for (value in values) {
                val valueSerializer = provider.findValueSerializer(value.javaClass)
                jgen.writeFieldName(fieldName)
                valueSerializer.serialize(value, jgen, provider)
            }
            jgen.writeEndObject()
            jgen.writeEndArray()
        } else {
            jgen.writeString("") // Generates tag for empty list
        }
    }
}
