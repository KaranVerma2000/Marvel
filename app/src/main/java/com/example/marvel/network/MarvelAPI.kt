package com.example.marvel.network

import com.example.marvel.model.Characters
import com.example.marvel.model.Comics
import com.example.marvel.model.Wrapper
import com.example.marvel.utils.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelAPI {

    @GET("characters")
    suspend fun getCharacters(
        @Query(publicKey) apiKey: String,
        @Query(md5hash) hash: String,
        @Query(timestamp) ts: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 10
    ): Response<Wrapper<Characters>>

    @GET("characters")
    suspend fun getNameCharacters(
        @Query(publicKey) apiKey: String,
        @Query(md5hash) hash: String,
        @Query(timestamp) ts: String,
        @Query(nameSearched) name: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 10
    ): Response<Wrapper<Characters>>

    @GET("comics")
    suspend fun getComics(
        @Query(publicKey) apiKey: String,
        @Query(md5hash) hash: String,
        @Query(timestamp) ts: String,
        @Query("limit") limit: Int = 40,
        @Query("offset") offset: Int = 220,
        @Query("orderBy") orderBy: String = comic_order_by
    ): Response<Wrapper<Comics>>
}