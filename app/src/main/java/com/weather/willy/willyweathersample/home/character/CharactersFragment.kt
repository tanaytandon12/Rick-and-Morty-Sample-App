package com.weather.willy.willyweathersample.home.character

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.weather.willy.willyweathersample.R
import kotlinx.android.synthetic.main.fragment_characters.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CharactersFragment : Fragment() {

    private val characterViewModel by activityViewModels<CharacterViewModel> { provideCharacterViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_characters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        characterViewModel.showProgress().observe(viewLifecycleOwner, Observer {
            pbCharacterList.visibility = if (it) View.VISIBLE else View.GONE
        })
    }
}