package com.example.marketplace_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marketplace_app.databinding.ActivityMainBinding
import com.example.marketplace_app.ui.fragments.ProductFragment
import com.example.marketplace_app.ui.fragments.ProductsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProductsFragment())
            .commit()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_products -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProductsFragment())
                        .commit()
                    true
                }
                R.id.navigation_cart -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProductFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}
