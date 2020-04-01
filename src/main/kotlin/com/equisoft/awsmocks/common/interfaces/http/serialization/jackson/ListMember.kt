package com.equisoft.awsmocks.common.interfaces.http.serialization.jackson

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@Retention
@JacksonAnnotationsInside
@JsonSerialize(using = ListMemberSerializer::class)
annotation class ListMember
