package com.weather.willy.willyweathersample.home.character

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.weather.willy.willyweathersample.R
import com.weather.willy.willyweathersample.model.local.Character
import kotlinx.android.synthetic.main.item_character.view.*

class CharacterAdapter(private val context: Context) :
    PagedListAdapter<Character, CharacterAdapter.CharacterViewHolder>(
        DIFF_CALLBACK
    ) {

    private val mLayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder =
        CharacterViewHolder(mLayoutInflater.inflate(R.layout.item_character, parent, false))

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Character>() {
            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem.id == newItem.id
        }
    }

    class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvName: TextView = itemView.tvTitle

        fun bind(position: Int, character: Character?) {
            tvName.text = character?.name
        }
    }
}