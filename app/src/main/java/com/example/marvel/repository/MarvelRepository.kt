package com.example.marvel.repository

import com.example.marvel.BuildConfig
import com.example.marvel.model.Characters
import com.example.marvel.model.Wrapper
import com.example.marvel.network.MarvelAPI
import com.example.marvel.network.Retrofit
import com.example.marvel.network.SafeApiRequest
import com.example.marvel.utils.getTs
import com.example.marvel.utils.md5Hash

class MarvelRepository : SafeApiRequest() {

    companion object {
        private val retrofit = Retrofit.getClient().create(MarvelAPI::class.java)
    }

    suspend fun getCharacters(): Wrapper<Characters> {
        val ts = getTs()
        val hash = md5Hash(ts + BuildConfig.PRIVATE_KEY + BuildConfig.API_KEY)
        return apiRequest { retrofit.getCharacters(BuildConfig.API_KEY, hash, ts) }
    }


}