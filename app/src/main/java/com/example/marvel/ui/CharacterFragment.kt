package com.example.marvel.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marvel.adapters.CharacterAdapter
import com.example.marvel.databinding.FragmentCharacterBinding
import com.example.marvel.utils.ApiException
import com.example.marvel.utils.hideKeyboard
import com.example.marvel.viewmodel.MarvelViewModel


class CharacterFragment : Fragment() {

    private lateinit var binding: FragmentCharacterBinding
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var viewModel: MarvelViewModel
    var nameSearch: String? = null


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
                    Log.d("live data character", it.value.toString())
                    characterAdapter.getList(it.value)
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
        if (viewModel.characters.value == null) {
            viewModel.getCharacters()
        }
        characterAdapter = CharacterAdapter()
        binding.characterRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = characterAdapter
        }

        binding.ivCancelSearch.setOnClickListener {
            binding.etSearch.setText("")
            viewModel.getCharacters()
            requireContext().hideKeyboard()
        }

        binding.etSearch.addTextChangedListener(textWatcher)

        binding.etSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.etSearch.text.toString().isNotEmpty()) {

                    Log.d("Search", binding.etSearch.text.toString())
                    viewModel.getNameCharacters(binding.etSearch.text.toString())
                    requireContext().hideKeyboard()
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }


    private val textWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!s.isNullOrEmpty()) {
                binding.ivCancelSearch.visibility = View.VISIBLE
            } else {
                binding.ivCancelSearch.visibility = View.GONE
                requireContext().hideKeyboard()
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
    }

}