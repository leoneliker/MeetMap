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
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ikalne.meetmap.MeetMapApplication.Companion.prefs
import com.ikalne.meetmap.ui.main.SectionsPagerAdapter
import com.ikalne.meetmap.databinding.ActivityMainAppBinding
import com.ikalne.meetmap.fragments.ChatFragment
import com.ikalne.meetmap.fragments.EditProfileFragment
import com.ikalne.meetmap.fragments.FavouritesFragment
import com.ikalne.meetmap.fragments.MapFragment


class MainAppActivity : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    lateinit var binding: ActivityMainAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lateinit var fStore: FirebaseFirestore
        lateinit var fAuth: FirebaseAuth
        lateinit var email: String
        supportActionBar?.hide()
        fStore = FirebaseFirestore.getInstance()
        fAuth = FirebaseAuth.getInstance()
        bottomNavView = binding.bottomNavigation

        var isnavview = false
        val mapFragment = MapFragment()
        val favFragment = FavouritesFragment()
        val chatFragment = ChatFragment()
        val profileFragment = EditProfileFragment()
        val navview = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navview.getHeaderView(0)
        val imagenav = headerView.findViewById<ImageView>(R.id.circle_image)
        val btnDeleteAccount = navview.findViewById<Button>(R.id.btnDeleteAccount)
        val animLeftNav = AnimationUtils.loadAnimation(this, R.anim.slidein)
        val slideout = AnimationUtils.loadAnimation(this, R.anim.slideout)
        email = prefs.getEmail()

        setThatFragment(mapFragment)
        Glide.with(this) //.load("https://images.unsplash.com/photo-1512849934327-1cf5bf8a5ccc?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80")
            .load(R.drawable.cara)
            .circleCrop()
            .into(imagenav)
        bottomNavView.setSelectedItemId(R.id.house)


        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.house -> {
                    binding.navView.isVisible = false
                    if (isnavview) {
                        slideout.interpolator = DecelerateInterpolator()
                        navview.startAnimation(slideout)
                        isnavview = false
                    }
                    setThatFragment(mapFragment)
                }
                R.id.likes -> {
                    binding.navView.isVisible = false
                    if (isnavview) {
                        slideout.interpolator = DecelerateInterpolator()
                        navview.startAnimation(slideout)
                        isnavview = false
                    }
                    setThatFragment(favFragment)
                }
                R.id.chat -> {
                    binding.navView.isVisible = false
                    if (isnavview) {
                        slideout.interpolator = DecelerateInterpolator()
                        navview.startAnimation(slideout)
                        isnavview = false
                    }
                    setThatFragment(chatFragment)
                }
                R.id.profile -> {
                    //setThatFragment(profileFragment)
                    binding.navView.isVisible = true
                    if (isnavview) {
                        slideout.interpolator = DecelerateInterpolator()
                        navview.startAnimation(slideout)
                        isnavview = false
                    } else {
                        isnavview = true
                        animLeftNav.interpolator = DecelerateInterpolator()
                        navview.startAnimation(animLeftNav)
                    }
                }
            }
            true
        }

        navview.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    if (isnavview) {
                        slideout.interpolator = DecelerateInterpolator()
                        navview.startAnimation(slideout)
                        isnavview = false
                    }
                    setThatFragment(profileFragment)

                }
                R.id.nav_notifications -> {
                    if (isnavview) {
                        slideout.interpolator = DecelerateInterpolator()
                        navview.startAnimation(slideout)
                        isnavview = false
                    }
                    //de momento no hace nada ver que pasa con esto
                }
                R.id.nav_manusu -> {
                    if (isnavview) {
                        slideout.interpolator = DecelerateInterpolator()
                        navview.startAnimation(slideout)
                        isnavview = false
                    }
                    //de momento no hace nada ver que pasa con esto
                    //crear el manual de usuario
                }
                R.id.nav_exit -> {
                    /* MeetMapApplication.prefs.wipe()
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)*/
                    MeetMapApplication.prefs.wipe()
                    fAuth.signOut()
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)

                   /* if (isnavview) {
                        slideout.interpolator = DecelerateInterpolator()
                        navview.startAnimation(slideout)
                        isnavview = false
                    }*/

                    /* setThatFragment(favFragment)*/


                }
            }
            // Indica que el elemento ha sido seleccionado
            menuItem.isChecked = true
            true
        }
        btnDeleteAccount.setOnClickListener {
            // Acción para el botón
           /* if (isnavview) {
                slideout.interpolator = DecelerateInterpolator()
                navview.startAnimation(slideout)
                isnavview = false
            }*/
            //setThatFragment(favFragment)
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete account")
            builder.setMessage("Are you sure you want to delete the account")
            builder.setPositiveButton("Accept") { dialog, which ->
                fStore.collection("users").document(email).delete()
                //Log.e("EEEEE", "Entra en fStore")
                fAuth.currentUser?.delete()
                //Log.e("AAAAAAA", "Entra en fStore")
                Toast.makeText(this, "The account has been deleted", Toast.LENGTH_LONG).show()
                MeetMapApplication.prefs.wipe()
                startActivity(Intent(this, Initial::class.java))

            }
            builder.setNegativeButton("Cancel"){dialog, which ->}
            builder.show()
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

            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                doubleBackToExitPressedOnce = false
            }, 2000)
        }

        private fun setThatFragment(fragment: Fragment) =
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame, fragment)
                commit()
            }

}
