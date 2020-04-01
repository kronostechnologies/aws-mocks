package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson

import com.equisoft.awsmocks.common.exceptions.Error
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer

class WrapErrorSerializer : JsonSerializer<Error>() {
    private val delegate = WrappedValueSerializer<Error>("Error")

    override fun serialize(value: Error, gen: JsonGenerator, serializers: SerializerProvider) =
        delegate.serialize(value, gen, serializers)

    override fun serializeWithType(
        value: Error,
        gen: JsonGenerator,
        serializers: SerializerProvider,
        typeSer: TypeSerializer
    ) = delegate.serializeWithType(value, gen, serializers, typeSer)
}
