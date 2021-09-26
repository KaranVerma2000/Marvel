package com.example.marvel.model

data class Wrapper<T: Any>(
    val data: Data<T>
)