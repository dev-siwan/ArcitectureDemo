package com.ksw.presentation.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.ksw.presentation.R
import com.ksw.presentation.ui.addfeeds.AddFeedFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.BLACK)

        //navController 를 통해 각 피드에 toolbar 설정
        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.feed_fragment_dest ->{
                    supportActionBar?.title = getString(R.string.feed_text)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.add_feed_fragment_dest ->{
                    supportActionBar?.title = getString(R.string.create_feed_text)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)

        fragment?.childFragmentManager?.fragments?.let {fragments->
            fragments.forEach {fragment->
                (fragment as AddFeedFragment).onActivityResult(requestCode,resultCode,data)
            }
        }

    }


}
