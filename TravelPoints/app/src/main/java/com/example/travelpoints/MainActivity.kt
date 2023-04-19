package com.example.travelpoints

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.travelpoints.databinding.MainActivityLayoutBinding
import com.example.travelpoints.ui.fragments.*
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityLayoutBinding

    private lateinit var loginFragment: LoginFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme(R.style.Theme_TravelPoints)

        setupFragmentNavigation()
    }

    private fun setupFragmentNavigation() {
        val accountFragment = AccountFragment(navigateToLoginFragment = {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, loginFragment).commit()
        })
        val mapFragment = MapFragment()
        val supportFragment = SupportFragment()
        val registerFragment = RegisterFragment(
            navigateToAccountFragment = {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, accountFragment).commit()
            }, navigateToLoginFragment = {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, loginFragment).commit()
            })
        loginFragment = LoginFragment(
            navigateToRegisterFragment = {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, registerFragment).commit()
            },
            navigateToAccountFragment = {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, accountFragment).commit()
            }
        )


        val bottomBar = binding.bottomNavigationView

        bottomBar.selectedItemId = R.id.map
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, mapFragment).commit()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.account -> {
                    if (FirebaseAuth.getInstance().currentUser != null) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, accountFragment)
                            .commit()
                    } else {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, loginFragment)
                            .commit()
                    }
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