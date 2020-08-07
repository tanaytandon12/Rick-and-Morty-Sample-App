package com.weather.willy.willyweathersample.home

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.weather.willy.willyweathersample.R
import com.weather.willy.willyweathersample.home.character.CharacterViewModel
import com.weather.willy.willyweathersample.home.character.provideCharacterViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {

    private val mCharacterViewModel: CharacterViewModel by viewModels<CharacterViewModel> {
        provideCharacterViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        subscribe()
    }

    private fun subscribe() {
        mCharacterViewModel.navigateOnCharacterSelected().observe(this, Observer {
            if (it) {
                toggleBackButton(
                    true,
                    mCharacterViewModel.selectedCharacterNameLiveData().value
                )
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        toggleBackButton(false, getString(R.string.app_name))
        super.onBackPressed()
    }

    private fun toggleBackButton(value: Boolean, title: String? = null) {
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(value)
        supportActionBar?.setDisplayShowHomeEnabled(value)
    }
}