package com.binaryflavor.jwtkeygenerator

import kotlinx.serialization.Serializable

@Serializable
data class EncodedKeyPairResult(
    val publicKey: String,
    val privateKey: String,
)
