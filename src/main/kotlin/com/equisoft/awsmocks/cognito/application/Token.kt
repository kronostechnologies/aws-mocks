package com.equisoft.awsmocks.cognito.application

data class Token(val accessToken: String, val expiresIn: Long, val tokenType: String = "Bearer")
