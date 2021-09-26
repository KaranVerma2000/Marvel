package com.example.marvel.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marvel.databinding.RecyclerItemViewBinding
import com.example.marvel.model.Characters
import com.example.marvel.utils.toUrl

class CharacterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<Characters> = arrayListOf()

    fun getList(characterList: ArrayList<Characters>) {
        Log.d("CharList", characterList.toString())
        this.list = characterList
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
        (holder as MarvelViewHolder).binding.itemHomeCardTitle.text = list[position].name
        Glide.with(holder.itemView).load(list[position].thumbnail.toUrl())
            .into(holder.binding.itemImage)
    }


    override fun getItemCount() = list.size


}