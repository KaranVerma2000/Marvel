package com.example.marvel.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.marvel.R
import com.example.marvel.databinding.ActivityMainBinding
import com.example.marvel.repository.MarvelRepository
import com.example.marvel.utils.ApiException
import com.example.marvel.viewmodel.MarvelViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var viewModel: MarvelViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        navController = findNavController(R.id.nav_host_fragment_user)
        binding.navViewUser.setupWithNavController(navController)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(
            MarvelViewModel::class.java
        )
    }

//    override fun onBackPressed() {
//        if (navController.currentDestination?.id!! == R.id.comicsFragment)
//            Log.d("Back press", viewModel._characters.toString())
//            viewModel._characters.postValue(ApiException.Success(viewModel.initialCharacterlist))
//        super.onBackPressed()
//    }
}