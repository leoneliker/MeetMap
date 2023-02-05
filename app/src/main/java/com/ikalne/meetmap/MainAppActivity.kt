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
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
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
        var isnavview= false
        val mapFragment = MapFragment()
        val favFragment = FavouritesFragment()
        val chatFragment = ChatFragment()
        val profileFragment = EditProfileFragment()
        val navview = findViewById<NavigationView>(R.id.nav_view)
        val animLeftNav = AnimationUtils.loadAnimation(this, R.anim.slidein)
        val slideout = AnimationUtils.loadAnimation(this, R.anim.slideout)

        setThatFragment(mapFragment)
        bottomNavView.setSelectedItemId(R.id.house)

        bottomNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.house ->{
                    binding.navView.isVisible= false
                    if(isnavview)
                    {
                        slideout.interpolator = DecelerateInterpolator()
                        navview.startAnimation(slideout)
                        isnavview=false
                    }
                    setThatFragment(mapFragment)
                }
                R.id.likes ->{
                    binding.navView.isVisible= false
                    if(isnavview)
                    {
                        slideout.interpolator = DecelerateInterpolator()
                        navview.startAnimation(slideout)
                        isnavview=false
                    }
                    setThatFragment(favFragment)
                }
                R.id.chat ->{
                    binding.navView.isVisible= false
                    if(isnavview)
                    {
                        slideout.interpolator = DecelerateInterpolator()
                        navview.startAnimation(slideout)
                        isnavview=false
                    }
                    setThatFragment(chatFragment)
                }
                R.id.profile ->{
                    //setThatFragment(profileFragment)
                    binding.navView.isVisible= true
                    isnavview=true
                    animLeftNav.interpolator = DecelerateInterpolator()
                    navview.startAnimation(animLeftNav)
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