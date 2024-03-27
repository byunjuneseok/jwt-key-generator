package com.binaryflavor.jwtkeygenerator

import kotlinx.serialization.Serializable

@Serializable
data class EncodedKeyResult(
    val key: String,
)
