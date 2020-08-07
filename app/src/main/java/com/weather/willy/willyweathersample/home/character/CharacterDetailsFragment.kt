package com.weather.willy.willyweathersample.home.character

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weather.willy.willyweathersample.R
import com.weather.willy.willyweathersample.model.local.Character
import com.weather.willy.willyweathersample.model.local.CharacterWithEpisode
import com.weather.willy.willyweathersample.model.local.Episode
import com.weather.willy.willyweathersample.util.showPlaceholderIfTextIsEmpty
import kotlinx.android.synthetic.main.fragment_character_details.*
import kotlinx.android.synthetic.main.item_episode_url.view.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CharacterDetailsFragment : Fragment() {

    val mCharacterViewModel: CharacterViewModel by activityViewModels {
        provideCharacterViewModelFactory()
    }

    private lateinit var mEpisodeUrlAdapter: EpisodeUrlAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        mCharacterViewModel.selectedCharacterLiveData().observe(viewLifecycleOwner, Observer {
            setup(it)
        })
    }

    private fun setupList() {
        rvUrls.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        rvUrls.setHasFixedSize(true)
        mEpisodeUrlAdapter = EpisodeUrlAdapter(requireContext())
        rvUrls.adapter = mEpisodeUrlAdapter
    }

    private fun setup(characterWithEpisode: CharacterWithEpisode) {
        Glide.with(ivCharacter).load(characterWithEpisode.character.image)
            .fitCenter().error(R.mipmap.ic_launcher_round).into(ivCharacter)
        showPlaceholderIfTextIsEmpty(
            requireContext(),
            tvName,
            R.string.name,
            characterWithEpisode.character.name,
            R.string.na
        )
        showPlaceholderIfTextIsEmpty(
            requireContext(),
            tvGender,
            R.string.gender,
            characterWithEpisode.character.gender,
            R.string.na
        )
        showPlaceholderIfTextIsEmpty(
            requireContext(),
            tvType,
            R.string.type,
            characterWithEpisode.character.type,
            R.string.na
        )

        showPlaceholderIfTextIsEmpty(
            requireContext(),
            tvStatus,
            R.string.status,
            characterWithEpisode.character.status,
            R.string.na
        )
        showPlaceholderIfTextIsEmpty(
            requireContext(),
            tvOrigin,
            R.string.origin,
            characterWithEpisode.character.origin,
            R.string.na
        )
        showPlaceholderIfTextIsEmpty(
            requireContext(),
            tvLocation,
            R.string.location,
            characterWithEpisode.character.location,
            R.string.na
        )

        mEpisodeUrlAdapter.setData(characterWithEpisode.episodes)

        tvEpisodeCount.text = requireContext().resources.getString(
            R.string.episodeCount,
            characterWithEpisode.episodes.size
        )
    }
}

class EpisodeUrlAdapter(context: Context) :
    RecyclerView.Adapter<EpisodeUrlAdapter.EpisodeUrlViewHolder>() {

    private val mLayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val mItems = mutableListOf<Episode>()

    fun setData(items: List<Episode>) {
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: EpisodeUrlViewHolder, position: Int) {
        holder.bind(position, mItems[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeUrlViewHolder =
        EpisodeUrlViewHolder(mLayoutInflater.inflate(R.layout.item_episode_url, parent, false))

    class EpisodeUrlViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUrl = itemView.tvUrl

        fun bind(position: Int, episode: Episode) {
            tvUrl.text = episode.url
        }
    }
}