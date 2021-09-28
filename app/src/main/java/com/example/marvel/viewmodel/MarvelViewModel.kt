package com.example.marvel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel.model.Characters
import com.example.marvel.model.Comics
import com.example.marvel.model.Wrapper
import com.example.marvel.repository.MarvelRepository
import com.example.marvel.utils.*
import kotlinx.coroutines.launch

class MarvelViewModel() : ViewModel() {

    private val repository = MarvelRepository()

    val _characters = MutableLiveData<ApiException<ArrayList<Characters>>>()
    val characters: LiveData<ApiException<ArrayList<Characters>>> = _characters

    private val _charactersName = MutableLiveData<ApiException<ArrayList<Characters>>>()
    val charactersName: LiveData<ApiException<ArrayList<Characters>>> = _charactersName

    private val _comics = MutableLiveData<ApiException<ArrayList<Comics>>>()
    val comics: LiveData<ApiException<ArrayList<Comics>>> = _comics

    var isAllLoaded : Boolean = false

    var initialCharacterlist: ArrayList<Characters> = arrayListOf()
    var changeCharacterlist: ArrayList<Characters> = arrayListOf()
    var initialComicList: ArrayList<Comics> = arrayListOf()
    var filteredComicList: ArrayList<Comics> = arrayListOf()
    var filter: Int = All
    var search: Boolean = false
    var nameSearch: String = ""


    fun getCharacters() {
        _characters.postValue(ApiException.Loading())
        viewModelScope.launch {
            try {
                Log.d("size", initialCharacterlist.size.toString())
                val result = repository.getCharacters(initialCharacterlist.size)
                handleCharacters(result)
            } catch (e: Exception) {
                Log.d("CharacterResult", e.message.toString())
                _characters.postValue(ApiException.Error(e.message.toString()))
            }
        }
    }

    private fun handleCharacters(result: Wrapper<Characters>) {
        if (result.data.results.size == 0){
            isAllLoaded = true
        }
        initialCharacterlist.addAll(result.data.results)
        Log.d("TAG", initialCharacterlist.toString())
        _characters.postValue(ApiException.Success(initialCharacterlist))
    }

    fun getNameCharacters(name: String) {
//        _characters.postValue(ApiException.Success(arrayListOf()))
        _characters.postValue(ApiException.Loading())
        viewModelScope.launch {
            try {
                Log.d("search size", changeCharacterlist.size.toString())
                val result = repository.getNameCharacters(changeCharacterlist.size, name)
                handleCharactersName(result)
            } catch (e: Exception) {
                Log.d("CharacterResultSearch", e.message.toString())
                _characters.postValue(ApiException.Error(e.message.toString()))
            }
        }
    }

    private fun handleCharactersName(result: Wrapper<Characters>) {
        val list = result.data.results
        if (result.data.results.size == 0){
            isAllLoaded = true
        }
        changeCharacterlist.addAll(list)
        Log.d(
            "TAG", "${changeCharacterlist.size} ${list.toString()}"
        )
        _characters.postValue(ApiException.Success(changeCharacterlist))
    }

    fun getComics() {
        _comics.postValue(ApiException.Loading())
        viewModelScope.launch {
            try {
                val result = repository.getComics()
                handleComics(result)
            } catch (e: Exception) {
                Log.d("ComicResult", e.message.toString())
                _comics.postValue(ApiException.Error(e.message.toString()))
            }
        }
    }

    private fun handleComics(result: Wrapper<Comics>) {
        val list = result.data.results
        initialComicList.clear()
        initialComicList.addAll(list)
        Log.d("Comics ", list.toString())
        _comics.postValue(ApiException.Success(list))
    }

//    private fun handleNameCharacters(result: Wrapper<Characters>) {
//        val list = result.data.results
//        Log.d("TAG", list.toString())
//        _characters.postValue(ApiException.Success(list))
//    }

    fun filterComics(type: Int) {
        filter = type
        Log.d("filter", type.toString())
        when (type) {
            All -> {
                _comics.postValue(ApiException.Success(initialComicList))
                return
            }
            THIS_WEEK -> {
                filteredComicList.clear()
                for (i in initialComicList)
                    if (inThisWeek(i.getDate()))
                        filteredComicList.add(i)
            }
            LAST_WEEK -> {
                filteredComicList.clear()
                for (i in initialComicList)
                    if (inLastWeek(i.getDate()))
                        filteredComicList.add(i)
            }
            NEXT_WEEK -> {
                filteredComicList.clear()
                for (i in initialComicList)
                    if (inNextWeek(i.getDate()))
                        filteredComicList.add(i)
            }
            THIS_MONTH -> {
                filteredComicList.clear()
                for (i in initialComicList)
                    if (inThisMonth(i.getDate()))
                        filteredComicList.add(i)
            }
        }
        _comics.postValue(ApiException.Success(filteredComicList))
    }


}
