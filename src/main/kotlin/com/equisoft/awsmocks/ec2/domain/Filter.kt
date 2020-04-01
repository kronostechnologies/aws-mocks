package com.equisoft.awsmocks.ec2.domain

import com.amazonaws.services.ec2.model.Filter

fun Filter.matches(testValue: String): Boolean =
    values.any { testValue.matches(it.replace("*", ".*?").toRegex()) }

fun List<Filter>.find(name: String): Filter? = find { it.name == name }
