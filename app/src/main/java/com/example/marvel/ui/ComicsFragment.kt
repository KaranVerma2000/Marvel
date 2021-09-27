package com.example.marvel.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marvel.R
import com.example.marvel.adapters.ComicsAdapter
import com.example.marvel.databinding.FragmentComicsBinding
import com.example.marvel.utils.*
import com.example.marvel.viewmodel.MarvelViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar


class ComicsFragment : Fragment() {

    private lateinit var binding: FragmentComicsBinding
    private val viewModel: MarvelViewModel by activityViewModels()
    private lateinit var comicsAdapter: ComicsAdapter
    lateinit var filter: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComicsBinding.inflate(inflater)
        filter = BottomSheetDialog(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.filter_layout, null)
        filter.setContentView(view)
        filter.setCancelable(false)

        binding.filterBtn.setOnClickListener {
            filter.show()
            sortByFilter()
        }

        initUI()
        observers()
        return binding.root
    }

    private fun sortByFilter() {
        val thisWeektxt = filter.findViewById<TextView>(R.id.thisWeek)
        val lastWeektxt = filter.findViewById<TextView>(R.id.lastWeek)
        val nextWeektxt = filter.findViewById<TextView>(R.id.nextWeek)
        val lastMonthtxt = filter.findViewById<TextView>(R.id.lastMonth)
        val lastMonthCard = filter.findViewById<CardView>(R.id.lastMonthCard)
        val lastWeekCard = filter.findViewById<CardView>(R.id.lastWeekCard)
        val thisWeekCard = filter.findViewById<CardView>(R.id.thisWeekCard)
        val nextWeekCard = filter.findViewById<CardView>(R.id.nextWeekCard)
        val AllCard = filter.findViewById<CardView>(R.id.AllCard)

        if (viewModel.filter == All)
            AllCard?.background!!.setTint(Color.parseColor("#e23636"))

        thisWeekCard?.setOnClickListener {
            thisWeekCard.background.setTint(Color.parseColor("#e23636"))
            lastWeekCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            lastMonthCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            nextWeekCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            AllCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            viewModel.filterComics(THIS_WEEK)
            filter.dismiss()
        }

        lastWeekCard?.setOnClickListener {
            thisWeekCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            lastWeekCard?.background!!.setTint(Color.parseColor("#e23636"))
            lastMonthCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            AllCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            nextWeekCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            viewModel.filterComics(LAST_WEEK)
            filter.dismiss()
        }
        lastMonthCard?.setOnClickListener {
            thisWeekCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            lastWeekCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            lastMonthCard?.background!!.setTint(Color.parseColor("#e23636"))
            nextWeekCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            AllCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            viewModel.filterComics(THIS_MONTH)
            filter.dismiss()
        }
        nextWeekCard?.setOnClickListener {
            thisWeekCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            lastWeekCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            lastMonthCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            nextWeekCard?.background!!.setTint(Color.parseColor("#e23636"))
            AllCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            viewModel.filterComics(NEXT_WEEK)
            filter.dismiss()
        }

        AllCard?.setOnClickListener {
            thisWeekCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            lastWeekCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            lastMonthCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            nextWeekCard?.background!!.setTint(Color.parseColor("#DEE0DC"))
            AllCard?.background!!.setTint(Color.parseColor("#e23636"))
            viewModel.filterComics(All)
            filter.dismiss()
        }


//        when (viewModel.filter) {
//            All -> {
//                thisWeekCard?.background!!.setTint(Color.parseColor("#57151920"))
//                lastWeekCard?.background!!.setTint(Color.parseColor("#57151920"))
//                lastMonthCard?.background!!.setTint(Color.parseColor("#57151920"))
//                nextWeekCard?.background!!.setTint(Color.parseColor("#57151920"))
//            }
//            THIS_WEEK -> {
//                thisWeekCard?.background!!.setTint(Color.parseColor("#e23636"))
//                lastWeekCard?.background!!.setTint(Color.parseColor("#57151920"))
//                lastMonthCard?.background!!.setTint(Color.parseColor("#57151920"))
//                nextWeekCard?.background!!.setTint(Color.parseColor("#57151920"))
//            }
//            LAST_WEEK -> {
//                thisWeekCard?.background!!.setTint(Color.parseColor("#57151920"))
//                lastWeekCard?.background!!.setTint(Color.parseColor("#e23636"))
//                lastMonthCard?.background!!.setTint(Color.parseColor("#57151920"))
//                nextWeekCard?.background!!.setTint(Color.parseColor("#57151920"))
//            }
//            NEXT_WEEK -> {
//                thisWeekCard?.background!!.setTint(Color.parseColor("#57151920"))
//                lastWeekCard?.background!!.setTint(Color.parseColor("#57151920"))
//                lastMonthCard?.background!!.setTint(Color.parseColor("#57151920"))
//                nextWeekCard?.background!!.setTint(Color.parseColor("#e23636"))
//            }
//            THIS_MONTH -> {
//                thisWeekCard?.background!!.setTint(Color.parseColor("#57151920"))
//                lastWeekCard?.background!!.setTint(Color.parseColor("#57151920"))
//                lastMonthCard?.background!!.setTint(Color.parseColor("#e23636"))
//                nextWeekCard?.background!!.setTint(Color.parseColor("#57151920"))
//            }
//        }
    }

    private fun observers() {
        viewModel.comics.observe(viewLifecycleOwner, {
            when (it) {
                is ApiException.Success -> {
                    comicsAdapter.getList(it.value)
                }
                is ApiException.Loading -> {

                }
                is ApiException.Error -> {
                    Snackbar.make(binding.root, it.message.toString(), 1000).show()
                }
            }
        })
    }

    private fun initUI() {

        viewModel.getComics()
        comicsAdapter = ComicsAdapter()
        binding.comicsRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = comicsAdapter
        }
    }


}