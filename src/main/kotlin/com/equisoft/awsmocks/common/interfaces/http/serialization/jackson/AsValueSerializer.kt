package com.equisoft.awsmocks.common.interfaces.http.serialization.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer

class AsValueSerializer : JsonSerializer<Boolean>() {
    private val delegate = WrappedValueSerializer<Boolean>("value")

    override fun serialize(value: Boolean, gen: JsonGenerator, serializers: SerializerProvider) =
        delegate.serialize(value, gen, serializers)

    override fun serializeWithType(
        value: Boolean,
        gen: JsonGenerator,
        serializers: SerializerProvider,
        typeSer: TypeSerializer
    ) = delegate.serializeWithType(value, gen, serializers, typeSer)
}
