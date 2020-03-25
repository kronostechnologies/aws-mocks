package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer

class WrappedValueSerializer<T : Any>(private val innerName: String) : JsonSerializer<T>() {
    override fun serialize(value: T, gen: JsonGenerator, serializers: SerializerProvider) =
        serializeWithType(value, gen, serializers, null)

    override fun serializeWithType(
        value: T,
        gen: JsonGenerator,
        serializers: SerializerProvider,
        typeSer: TypeSerializer?
    ) {
        gen.writeStartArray(1)
        gen.writeStartObject()

        val valueSerializer = serializers.findValueSerializer(value.javaClass)
        gen.writeFieldName(innerName)
        valueSerializer.serialize(value, gen, serializers)

        gen.writeEndObject()
        gen.writeEndArray()
    }
}
