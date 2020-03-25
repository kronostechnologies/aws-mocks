package com.equisoft.awsmocks.common.infrastructure.aws

inline fun <reified T> readResource(service: AwsService, name: String = T::class.java.simpleName): T {
    return T::class.java.getResourceAsStream("/${service.name.toLowerCase()}/$name.json")
        .let { AwsObjectMapper.fromStream(it, T::class.java) }
}
