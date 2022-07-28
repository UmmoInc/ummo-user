package xyz.ummo.bite.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import xyz.ummo.bite.R
import xyz.ummo.bite.sharedpreferences.Prefs


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFM = supportFragmentManager




    }
    companion object {

        // tags used to attach the fragments

        lateinit var supportFM: FragmentManager



    }

}