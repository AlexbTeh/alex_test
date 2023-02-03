package com.test_crypto

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setNavigation()
        viewModel.syncDataEveryMinute()
    }


    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
    }

    private fun setNavigation() {
        runOnUiThread {
            val navGraph = navController.navInflater.inflate(R.navigation.main_navigation)
            navGraph.setStartDestination(com.test_crypto.feature_home.R.id.home_navigation)
            navGraph.let {
                navController.setGraph(it, null)
            }
        }
    }
}