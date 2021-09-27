package com.example.marvel.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.example.marvel.model.Comics
import com.example.marvel.model.Thumbnail
import java.math.BigInteger
import java.security.MessageDigest
import java.time.DayOfWeek
import java.time.LocalDate

const val base_url = "https://gateway.marvel.com/v1/public/"
const val publicKey = "apikey"
const val md5hash = "hash"
const val timestamp = "ts"
const val resource_type = "/portrait_fantastic"
const val order_by = "orderBy"
const val nameSearched = "nameStartsWith"
const val comic_order_by = "-onsaleDate"

const val All = 0
const val THIS_WEEK = 1
const val NEXT_WEEK = 2
const val LAST_WEEK = 3
const val THIS_MONTH = 4


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

fun Comics.getDate(): String {
    for (i in this.dates)
        if (i.type == "onsaleDate")
            return i.date
    return ""
}

fun inThisWeek(str: String): Boolean {

    val today: LocalDate = LocalDate.now()

    var sunday: LocalDate = today
    while (sunday.dayOfWeek !== DayOfWeek.SUNDAY) {
        sunday = sunday.minusDays(1)
    }

    var saturday: LocalDate = today
    while (saturday.dayOfWeek !== DayOfWeek.SATURDAY) {
        saturday = saturday.plusDays(1)
    }

    return str >= sunday.toString() && str <= saturday.toString()
}

fun inLastWeek(str: String): Boolean {

    val today: LocalDate = LocalDate.now()

    var sunday: LocalDate = today.minusDays(7)
    while (sunday.dayOfWeek !== DayOfWeek.SUNDAY) {
        sunday = sunday.minusDays(1)
    }

    var saturday: LocalDate = today
    while (saturday.dayOfWeek !== DayOfWeek.SATURDAY) {
        saturday = saturday.plusDays(1)
    }

    return str >= sunday.toString() && str <= saturday.toString()
}

fun inNextWeek(str: String): Boolean {

    val today: LocalDate = LocalDate.now()

    var sunday: LocalDate = today
    while (sunday.dayOfWeek !== DayOfWeek.SUNDAY) {
        sunday = sunday.plusDays(1)
    }

    var saturday: LocalDate = sunday
    while (saturday.dayOfWeek !== DayOfWeek.SATURDAY) {
        saturday = saturday.plusDays(1)
    }

    return str >= sunday.toString() && str <= saturday.toString()
}

fun inThisMonth(str: String): Boolean {
    val today = LocalDate.now().toString()
    return str[5] == today[5] && str[6] == today[6]
}