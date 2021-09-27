package com.example.marvel.model

data class Comics(
    val dates: ArrayList<Date>,
    val thumbnail: Thumbnail,
    val title: String
)

data class Date(
    val date: String,
    val type: String
)