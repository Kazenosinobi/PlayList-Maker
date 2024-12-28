package com.practicum.playlistmaker.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding?.bottomNavigationView?.setupWithNavController(navController)

        navController.addOnDestinationChangedListener {_, destination, _ ->
            when (destination.id) {
                R.id.searchFragment -> {
                    binding?.bottomNavigationView?.visibility = View.VISIBLE
                }
                R.id.mediaLibraryFragment -> {
                    binding?.bottomNavigationView?.visibility = View.VISIBLE
                }
                R.id.settingsFragment -> {
                    binding?.bottomNavigationView?.visibility = View.VISIBLE
                }
                else -> {
                    binding?.bottomNavigationView?.visibility = View.GONE
                }
            }
        }
    }
}