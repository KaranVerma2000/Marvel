package com.example.marvel.utils

import java.math.BigInteger
import java.security.MessageDigest

const val base_url = "https://gateway.marvel.com/v1/public/"
const val publicKey = "apikey"
const val md5hash = "hash"
const val timestamp = "ts"


fun md5Hash(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}

fun getTs() = (System.currentTimeMillis() / 1000).toString()