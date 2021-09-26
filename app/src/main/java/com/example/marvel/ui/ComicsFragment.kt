package com.example.marvel.ui

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.marvel.R
import com.example.marvel.databinding.FragmentCharacterBinding
import com.example.marvel.databinding.FragmentComicsBinding


class ComicsFragment : Fragment() {

  private  lateinit var binding: FragmentComicsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComicsBinding.inflate(inflater)
        return binding.root
    }

}