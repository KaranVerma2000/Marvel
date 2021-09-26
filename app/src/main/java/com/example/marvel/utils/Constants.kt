package com.example.marvel.utils

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.example.marvel.model.Thumbnail
import com.example.marvel.model.Wrapper
import retrofit2.Response
import java.math.BigInteger
import java.security.MessageDigest

const val base_url = "https://gateway.marvel.com/v1/public/"
const val publicKey = "apikey"
const val md5hash = "hash"
const val timestamp = "ts"
const val resource_type = "/portrait_fantastic"
const val order_by = "orderBy"
const val nameSearched = "nameStartsWith"


fun md5Hash(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}

fun getTs() = (System.currentTimeMillis() / 1000).toString()

fun Thumbnail.toUrl(): String {
    return this.path + resource_type + "." + this.extension
}

fun Context.hideKeyboard() {
    val inputMethodManager: InputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)
}