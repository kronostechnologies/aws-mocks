package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@Retention
@JacksonAnnotationsInside
@JsonSerialize(using = ListItemSerializer::class)
annotation class ListItem(val value: String)
