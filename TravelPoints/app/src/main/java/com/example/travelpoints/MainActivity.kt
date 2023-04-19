package com.example.travelpoints

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.travelpoints.databinding.MainActivityLayoutBinding
import com.example.travelpoints.ui.fragments.AccountFragment
import com.example.travelpoints.ui.fragments.MapFragment
import com.example.travelpoints.ui.fragments.SupportFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme(R.style.Theme_TravelPoints)

        setupFragmentNavigation()
    }

    private fun setupFragmentNavigation() {
        val accountFragment = AccountFragment()
        val mapFragment = MapFragment()
        val supportFragment = SupportFragment()

        val bottomBar = binding.bottomNavigationView

        bottomBar.selectedItemId = R.id.map
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, mapFragment).commit()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.account -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, accountFragment)
                        .commit()
                }
                R.id.map -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, mapFragment)
                        .commit()
                }
                R.id.support -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, supportFragment)
                        .commit()
                }
            }
            true
        }
    }
}