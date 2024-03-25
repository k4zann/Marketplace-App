package com.example.marketplace_app.ui

import DataCheckWorker
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.marketplace_app.R
import com.example.marketplace_app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startPeriodicDataCheck()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.cartFragment -> {
                    Log.d("MainActivity", "Cart Fragment")
                    navController.navigate(R.id.cartFragment)
                    true
                }
                R.id.productFragment -> {
                    Log.d("MainActivity", "Products Fragment")
                    navController.navigate(R.id.productsFragment)
                    true
                }
                else -> {
                    Log.d("MainActivity", "Default Fragment")
                    false
                }
            }
        }
    }
    private fun startPeriodicDataCheck() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val dataCheckRequest = PeriodicWorkRequestBuilder<DataCheckWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "data_check_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            dataCheckRequest
        )
    }
}

