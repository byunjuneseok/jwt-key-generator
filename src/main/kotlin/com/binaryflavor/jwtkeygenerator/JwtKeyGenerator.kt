package com.binaryflavor.jwtkeygenerator

import io.jsonwebtoken.Jwts
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bouncycastle.openssl.jcajce.JcaPEMWriter
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.StringWriter
import java.security.Key
import java.security.KeyPair
import java.util.*
import javax.crypto.SecretKey

class JwtKeyGenerator() {
    private fun encodeSecretKey(key: SecretKey): EncodedKeyResult {
        val keyBytes = key.encoded
        Base64.getEncoder().encodeToString(keyBytes).let {
            return EncodedKeyResult(key = it)
        }
    }

    private fun encodeKeyPair(keyPair: KeyPair): EncodedKeyPairResult {
        return EncodedKeyPairResult(
            publicKey = Base64.getEncoder().encodeToString(keyPair.public.encoded),
            privateKey = Base64.getEncoder().encodeToString(keyPair.private.encoded),
        )
    }

    fun generateKeyForHS256(): EncodedKeyResult {
        Jwts.SIG.HS256.key().build().let { key ->
            val output = makeDirectory(prefix = Algorithms.HS256.name.lowercase())
            val encoded = encodeSecretKey(key)
            saveEncodedKeyResult(encoded, output)
            return encoded
        }
    }

    fun generateKeyForHS384(): EncodedKeyResult {
        Jwts.SIG.HS384.key().build().let { key ->
            val output = makeDirectory(prefix = Algorithms.HS384.name.lowercase())
            val encoded = encodeSecretKey(key)
            saveEncodedKeyResult(encoded, output)
            return encoded
        }
    }

    fun generateKeyForHS512(): EncodedKeyResult {
        Jwts.SIG.HS512.key().build().let { key ->
            val output = makeDirectory(prefix = Algorithms.HS512.name.lowercase())
            val encoded = encodeSecretKey(key)
            saveEncodedKeyResult(encoded, output)
            return encoded
        }
    }

    fun generateKeysForRS256(): EncodedKeyPairResult {
        Jwts.SIG.RS256.keyPair().build().let { keypair ->
            makeDirectory(prefix = Algorithms.RS256.name.lowercase()).let { output ->
                saveKeyPair(keypair, output)
                val encoded = encodeKeyPair(keypair)
                saveEncodedKeyPairResult(encoded, output)
                return encoded
            }
        }
    }

    fun generateKeysForRS512(): EncodedKeyPairResult {
        Jwts.SIG.RS512.keyPair().build().let { key ->
            makeDirectory(prefix = Algorithms.RS512.name.lowercase()).let { output ->
                saveKeyPair(key, output)
                val encoded = encodeKeyPair(key)
                saveEncodedKeyPairResult(encoded, output)
                return encoded
            }
        }
    }

    private fun saveKeyToPemFile(
        key: Key,
        output: String,
    ) {
        StringWriter().use { sw ->
            JcaPEMWriter(sw).use { pemWriter ->
                pemWriter.writeObject(key)
                pemWriter.flush()
                val pem = sw.toString()
                DataOutputStream(FileOutputStream(output)).use { dos ->
                    dos.write(pem.toByteArray())
                }
            }
        }
    }

    private fun makeDirectory(prefix: String): String {
        val timestamp = Date().time
        val output = "results/$prefix-$timestamp/"
        val folder = File(output)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return output
    }

    private fun saveKeyPair(
        keyPair: KeyPair,
        output: String = "results/",
    ) {
        val publicKey = keyPair.public
        val privateKey = keyPair.private
        saveKeyToPemFile(publicKey, "$output/public.pem")
        saveKeyToPemFile(privateKey, "$output/private.pem")
    }

    private fun saveOutputString(
        content: String,
        output: String,
    ) {
        DataOutputStream(
            FileOutputStream(
                "$output/base64.json",
            ),
        ).use { dos ->
            dos.write(content.toByteArray())
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun getPrettyPrintJson(): Json {
        return Json {
            prettyPrint = true
            prettyPrintIndent = "  "
        }
    }

    private fun saveEncodedKeyPairResult(
        keyPair: EncodedKeyPairResult,
        output: String,
    ) {
        saveOutputString(getPrettyPrintJson().encodeToString(keyPair), output)
    }

    private fun saveEncodedKeyResult(
        key: EncodedKeyResult,
        output: String,
    ) {
        saveOutputString(getPrettyPrintJson().encodeToString(key), output)
    }
}
