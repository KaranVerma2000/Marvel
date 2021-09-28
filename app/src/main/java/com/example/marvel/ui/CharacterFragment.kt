package com.example.marvel.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marvel.R
import com.example.marvel.adapters.CharacterAdapter
import com.example.marvel.databinding.FragmentCharacterBinding
import com.example.marvel.model.Characters
import com.example.marvel.utils.ApiException
import com.example.marvel.utils.hideKeyboard
import com.example.marvel.viewmodel.MarvelViewModel


class CharacterFragment : Fragment() {

    private lateinit var binding: FragmentCharacterBinding
    private lateinit var characterAdapter: CharacterAdapter
    private val viewModel: MarvelViewModel by activityViewModels()
    var nameSearch: String? = null
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false
    private val list: ArrayList<Characters> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterBinding.inflate(inflater)
        initUI()
        observers()
        return binding.root
    }

    private fun observers() {
        viewModel.characters.observe(viewLifecycleOwner, {
            when (it) {
                is ApiException.Success -> {
                    binding.gif.visibility = View.GONE
                    Log.d("live data character", it.value.toString())
                    Log.d("list size", list.size.toString())
                    Log.d("livedata size", it.value.size.toString())
                    if (viewModel.isAllLoaded) {
                        isLastPage = true
                        return@observe
                    }
                    list.clear()
                    list.addAll(it.value)
                    characterAdapter.getList(it.value)
                    isLoading = false
                }
                is ApiException.Loading -> {
                    binding.gif.visibility = View.VISIBLE
                    Glide.with(binding.root).load(R.raw.loading).into(binding.gif)
                    isLoading = true
                }
                is ApiException.Error -> {
                    binding.gif.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun initUI() {

        viewModel.getCharacters()
        characterAdapter = CharacterAdapter()
        binding.characterRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = characterAdapter
        }
        binding.characterRV.addOnScrollListener(scrollListener)

        binding.ivCancelSearch.setOnClickListener {
            binding.etSearch.setText("")
            viewModel.search = false
            viewModel.nameSearch = ""
            viewModel.getCharacters()
            binding.characterRV.smoothScrollToPosition(0)
            requireContext().hideKeyboard()
        }

        binding.etSearch.addTextChangedListener(textWatcher)

        binding.etSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.etSearch.text.toString().isNotEmpty()) {
                    Log.d("Search", binding.etSearch.text.toString())
                    viewModel.search = true
                    isLoading = false
                    isLastPage = false
                    viewModel.isAllLoaded = false
                    viewModel.changeCharacterlist.clear()
                    viewModel.nameSearch = binding.etSearch.text.toString()
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

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val lastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val paginate =
                isNotLoadingAndNotLastPage && lastItem && isNotAtBeginning && isScrolling

            Log.d("Loading", "${isLoading}")
            Log.d("last page", "${isLastPage}")
            Log.d("last item", "$lastItem")
            Log.d("not begin", "$isNotAtBeginning")
            Log.d("is scroll", "$isScrolling")

            if (paginate) {
                if (viewModel.search) {
                    Log.d("paginate", viewModel.search.toString())
                    viewModel.getNameCharacters(viewModel.nameSearch)
                } else {
                    Log.d("paginate", viewModel.search.toString())
                    viewModel.getCharacters()
                }
                isScrolling = false
                Log.d("Scrolling", isScrolling.toString())
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
                Log.d("Scrolling", isScrolling.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("char", "destroy")
        viewModel.search = false
        viewModel.nameSearch = ""
        viewModel._characters.postValue(ApiException.Success(viewModel.initialCharacterlist))
    }

}