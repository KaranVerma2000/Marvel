package com.example.marvel.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marvel.databinding.RecyclerItemViewBinding
import com.example.marvel.model.Comics
import com.example.marvel.utils.toUrl

class ComicsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<Comics> = arrayListOf()

    fun getList(comicsList: ArrayList<Comics>) {
        Log.d("CharList", comicsList.toString())
        this.list = comicsList
        notifyDataSetChanged()
    }

    inner class MarvelViewHolder(var binding: RecyclerItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarvelViewHolder =
        MarvelViewHolder(
            RecyclerItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MarvelViewHolder).binding.itemHomeCardTitle.text = list[position].title
        Glide.with(holder.itemView).load(list[position].thumbnail.toUrl())
            .into(holder.binding.itemImage)
    }

    override fun getItemCount() = list.size
}