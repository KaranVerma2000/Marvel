package com.example.marvel.model

data class Data<T:Any>(
    val count: Int,
    val offset: Int,
    val results: ArrayList<T>
)