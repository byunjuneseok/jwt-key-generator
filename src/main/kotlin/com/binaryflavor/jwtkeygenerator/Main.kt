package com.binaryflavor.jwtkeygenerator

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

fun main(args: Array<String>) {
    val parser = ArgParser("JwtKeyGenerator")
    val algorithm by parser.option(
        ArgType.Choice<Algorithms>(),
        shortName = "a",
        description = "Format for output file",
    ).default(Algorithms.HS256)
    parser.parse(args)
    JwtKeyGenerator().let {
        when (algorithm) {
            Algorithms.HS256 -> {
                println(it.generateKeyForHS256())
            }
            Algorithms.HS384 -> {
                println(it.generateKeyForHS384())
            }
            Algorithms.HS512 -> {
                println(it.generateKeyForHS512())
            }
            Algorithms.RS256 -> {
                println(it.generateKeysForRS256())
            }
            Algorithms.RS512 -> {
                println(it.generateKeysForRS512())
            }
        }
    }
}
