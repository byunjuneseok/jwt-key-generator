package com.binaryflavor.jwtkeygenerator

import com.binaryflavor.com.binaryflavor.jwtkeygenerator.Algorithms
import com.binaryflavor.com.binaryflavor.jwtkeygenerator.JwtKeyGenerator
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

// get arguments
fun main(args: Array<String>) {
    val parser = ArgParser("JwtKeyGenerator")
    val algorithm by parser.option(
        ArgType.Choice<Algorithms>(),
        shortName = "a",
        description = "Format for output file",
    ).default(Algorithms.HS256)
    val generator = JwtKeyGenerator()
    parser.parse(args)
    println(algorithm)
    when (algorithm) {
        Algorithms.HS256 -> {
            println(generator.generateKeyForHS256())
        }
        Algorithms.HS384 -> {
            println(generator.generateKeyForHS384())
        }
        Algorithms.HS512 -> {
            println(generator.generateKeyForHS512())
        }
        Algorithms.RS256 -> {
            println(generator.generateKeysForRS256())
        }
        Algorithms.RS512 -> {
            println(generator.generateKeysForRS512())
        }
    }
}
