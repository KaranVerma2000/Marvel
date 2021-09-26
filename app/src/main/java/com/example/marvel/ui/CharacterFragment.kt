package com.example.marvel.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marvel.adapters.CharacterAdapter
import com.example.marvel.databinding.FragmentCharacterBinding
import com.example.marvel.utils.ApiException
import com.example.marvel.viewmodel.MarvelViewModel


class CharacterFragment : Fragment() {

    private lateinit var binding: FragmentCharacterBinding
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var viewModel: MarvelViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterBinding.inflate(inflater)
        initViewModel()
        initUI()
        observers()
        return binding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MarvelViewModel::class.java)
    }

    private fun observers() {
        viewModel.characters.observe(viewLifecycleOwner, {
            when (it) {
                is ApiException.Success -> {
                    if (it.value.size != 0) {
                        characterAdapter.getList(it.value)
                    }
                }
                is ApiException.Loading -> {

                }
                is ApiException.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun initUI() {

        viewModel.loadCharacters()
        characterAdapter = CharacterAdapter()
        binding.characterRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = characterAdapter
        }
    }
}