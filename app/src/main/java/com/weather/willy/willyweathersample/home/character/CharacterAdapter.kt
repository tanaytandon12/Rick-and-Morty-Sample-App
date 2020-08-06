package com.weather.willy.willyweathersample.home.character

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weather.willy.willyweathersample.R
import com.weather.willy.willyweathersample.model.local.Character
import com.weather.willy.willyweathersample.util.showPlaceholderIfTextIsEmpty
import kotlinx.android.synthetic.main.item_character.view.*

class CharacterAdapter(
    private val context: Context,
    private val onCharacterSelected: ((Int, Character?) -> Unit)? = null
) :
    PagedListAdapter<Character, CharacterAdapter.CharacterViewHolder>(
        DIFF_CALLBACK
    ) {

    private val mLayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder =
        CharacterViewHolder(
            mLayoutInflater.inflate(
                R.layout.item_character,
                parent,
                false
            )
        ) { position ->
            onCharacterSelected?.invoke(position, getItem(position))
        }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Character>() {
            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem.characterId == newItem.characterId
        }
    }

    class CharacterViewHolder(view: View, onCharacterSelected: ((Int) -> Unit)? = null) :
        RecyclerView.ViewHolder(view) {

        private val tvName: TextView = itemView.tvName
        private val ivCharacter: ImageView = itemView.ivCharacter
        private val tvType = itemView.tvType
        private val tvStatus = itemView.tvStatus
        private val tvGender = itemView.tvGender
        private val tvOrigin = itemView.tvOrigin
        private val tvLocation = itemView.tvLocation
        private val tvEpisodeCount = itemView.tvEpisodeCount

        init {
            itemView.setOnClickListener {
                val pos = it.tag as Int
                onCharacterSelected?.invoke(pos)
            }
        }

        fun bind(position: Int, character: Character?) {
            itemView.tag = position

            character?.image?.let {
                Glide.with(ivCharacter).load(it).into(ivCharacter)
            }

            tvEpisodeCount.text =
                itemView.context.resources.getString(R.string.episodeCount, character?.episodeCount)

            showPlaceholderIfTextIsEmpty(
                itemView.context,
                tvName,
                R.string.name,
                character?.name,
                R.string.na
            )

            showPlaceholderIfTextIsEmpty(
                itemView.context,
                tvType,
                R.string.type,
                character?.type,
                R.string.na
            )
            showPlaceholderIfTextIsEmpty(
                itemView.context,
                tvStatus,
                R.string.status,
                character?.status,
                R.string.na
            )
            showPlaceholderIfTextIsEmpty(
                itemView.context,
                tvGender,
                R.string.gender,
                character?.gender,
                R.string.na
            )
            showPlaceholderIfTextIsEmpty(
                itemView.context,
                tvOrigin,
                R.string.origin,
                character?.origin,
                R.string.na
            )
            showPlaceholderIfTextIsEmpty(
                itemView.context,
                tvLocation,
                R.string.location,
                character?.location,
                R.string.na
            )
        }


    }
}