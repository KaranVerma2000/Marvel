package com.example.marvel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel.model.Characters
import com.example.marvel.model.Wrapper
import com.example.marvel.repository.MarvelRepository
import com.example.marvel.utils.ApiException
import kotlinx.coroutines.launch

class MarvelViewModel : ViewModel() {

    private val repository = MarvelRepository()

    private val _characters = MutableLiveData<ApiException<ArrayList<Characters>>>()
    val characters: LiveData<ApiException<ArrayList<Characters>>> = _characters

    private val _charactersName = MutableLiveData<ApiException<ArrayList<Characters>>>()
    val charactersName: LiveData<ApiException<ArrayList<Characters>>> = _charactersName

    fun getCharacters() {
        _characters.postValue(ApiException.Loading())
        viewModelScope.launch {
            try {
                val result = repository.getCharacters()
                handleCharacters(result)
            } catch (e: Exception) {
                Log.d("CharacterResult", e.message.toString())
                _characters.postValue(ApiException.Error(e.message.toString()))
            }
        }
    }

    private fun handleCharacters(result: Wrapper<Characters>) {
        val list = result.data.results
        Log.d("TAG", list.toString())
        _characters.postValue(ApiException.Success(list))
    }

    fun getNameCharacters(name: String) {
        _characters.postValue(ApiException.Loading())
        viewModelScope.launch {
            try {
                val result = repository.getNameCharacters(name)
                handleCharacters(result)
            } catch (e: Exception) {
                Log.d("CharacterResultSearch", e.message.toString())
                _characters.postValue(ApiException.Error(e.message.toString()))
            }
        }
    }

//    private fun handleNameCharacters(result: Wrapper<Characters>) {
//        val list = result.data.results
//        Log.d("TAG", list.toString())
//        _characters.postValue(ApiException.Success(list))
//    }
}
