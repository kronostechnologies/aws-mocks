package com.equisoft.awsmocks.cognito.interfaces.http.serialization.jackson

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
interface TokenMixin
