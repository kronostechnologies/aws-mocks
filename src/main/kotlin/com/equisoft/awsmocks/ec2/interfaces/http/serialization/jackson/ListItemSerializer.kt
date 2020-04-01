package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

/**
 * This class is used to bypass Jackson limitation with XmlElementWrapper and XmlElements.
 * Jackson complains multiple properties are named "item" although there's a wrapper around it.
 */
class ListItemSerializer : JsonSerializer<List<Any>>() {
    override fun serialize(values: List<Any>, jgen: JsonGenerator, provider: SerializerProvider) {
        if (values.isNotEmpty()) {
            jgen.writeStartArray(values.size)
            for (value in values) {
                jgen.writeStartObject()

                val valueSerializer = provider.findValueSerializer(value.javaClass)
                jgen.writeFieldName("item")
                valueSerializer.serialize(value, jgen, provider)

                jgen.writeEndObject()
            }
            jgen.writeEndArray()
        } else {
            jgen.writeString("") // Generates tag for empty list
        }
    }
}
