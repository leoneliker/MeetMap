package com.ikalne.meetmap

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ikalne.meetmap.ui.main.SectionsPagerAdapter
import com.ikalne.meetmap.databinding.ActivityMainAppBinding
import com.ikalne.meetmap.fragments.ChatFragment
import com.ikalne.meetmap.fragments.EditProfileFragment
import com.ikalne.meetmap.fragments.FavouritesFragment
import com.ikalne.meetmap.fragments.MapFragment


class MainAppActivity : AppCompatActivity() {
    private lateinit var bottomNavView : BottomNavigationView
    lateinit var binding: ActivityMainAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        bottomNavView =binding.bottomNavigation

        val mapFragment = MapFragment()
        val favFragment = FavouritesFragment()
        val chatFragment = ChatFragment()
        val profileFragment = EditProfileFragment()

        setThatFragment(mapFragment)
        bottomNavView.setSelectedItemId(R.id.house)

        bottomNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.house ->{
                    setThatFragment(mapFragment)
                }
                R.id.likes ->{
                    setThatFragment(favFragment)
                }
                R.id.chat ->{
                    setThatFragment(chatFragment)
                }
                R.id.profile ->{
                    setThatFragment(profileFragment)
                }
            }
            true
        }


    }
    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            return
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun setThatFragment(fragment : Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame, fragment)
            commit()
        }

}