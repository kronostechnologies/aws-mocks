package com.equisoft.awsmocks.cognito.interfaces.http.serialization.jackson

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
interface TokenMixin
