package com.ikalne.meetmap

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ikalne.meetmap.databinding.ActivityMainAppBinding
import com.ikalne.meetmap.fragments.*


class MainAppActivity : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    lateinit var binding: ActivityMainAppBinding
    private lateinit var imageButton: ImageButton
    private lateinit var transparentButton: Button
    private lateinit var frame: FrameLayout
    private lateinit var navView :NavigationView
    lateinit var fStorage: StorageReference
    private val handler = Handler()
    private var isnavview = false
    private lateinit var navview : NavigationView


     override fun onCreate(savedInstanceState: Bundle?) {
         forceLightMode()
        super.onCreate(savedInstanceState)
        binding = ActivityMainAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lateinit var fStore: FirebaseFirestore
        lateinit var fAuth: FirebaseAuth
        lateinit var email: String
        supportActionBar?.hide()
        fStorage = FirebaseStorage.getInstance().getReference()
        fStore = FirebaseFirestore.getInstance()
        fAuth = FirebaseAuth.getInstance()
        bottomNavView = binding.bottomNavigation
        transparentButton = binding.btntransparent
        imageButton = binding.imageButton
        frame=binding.frame
        navView=binding.navView


        val mapFragment = MapFragment()
        val favFragment = FavouritesFragment()
        val chatFragment = ChatFragment()
        val profileFragment = EditProfileFragment()
        navview = findViewById<NavigationView>(R.id.nav_view)
        val faqsFragment = FaqsFragment()
        val contactUsFragment = ConctactUsFragment()
        val notificationsFragment = NotificationsFragment()
        val navview = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navview.getHeaderView(0)
        val imagenav = headerView.findViewById<ImageView>(R.id.circle_image)
        val btnDeleteAccount = navview.findViewById<Button>(R.id.btnDeleteAccount)
        val slidein = AnimationUtils.loadAnimation(this, R.anim.slidein)


        email = PreferencesManager.getDefaultSharedPreferences(this).getEmail()
        transparentButton.visibility = View.GONE

        setThatFragment(mapFragment)
        Glide.with(this) //.load("https://images.unsplash.com/photo-1512849934327-1cf5bf8a5ccc?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80")
            .load(R.drawable.cara)
            .circleCrop()
            .into(imagenav)
        bottomNavView.setSelectedItemId(R.id.house)

        imageButton.setOnClickListener()
        {
            binding.navView.isVisible = true
            isnavview = true
            slidein.interpolator = DecelerateInterpolator()
            binding.navView.startAnimation(slidein)
            imageButton.visibility = View.GONE
            transparentButton.visibility = View.VISIBLE
        }

        transparentButton.setOnClickListener()
        {
            isnavview = false
            animateAndHideNavigationView(navview)
            buttonsVisibility()
            /*binding.navView.setCheckedItem(-1)
            binding.navView.itemIconTintList = null
            binding.navView.itemTextColor = null
            binding.navView.menu.setGroupCheckable(0, false, true)*/

        }
        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.house -> {
                    closeNav()
                    setThatFragment(mapFragment)

                }
                R.id.likes -> {
                    closeNav()
                    setThatFragment(favFragment)
                }
                R.id.chat -> {
                    closeNav()
                    setThatFragment(chatFragment)
                }
            }
            true
        }

        navview.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    closeNav()
                    setThatFragment(profileFragment)

                }
                R.id.nav_notifications -> {
                    closeNav()
                    setThatFragment(notificationsFragment)
                }
                R.id.nav_manusu -> {
                    closeNav()
                    setThatFragment(faqsFragment)
                }
                R.id.contactus -> {
                    closeNav()
                    navview.visibility = View.GONE
                }
                R.id.nav_manusu -> {
                    closeNav()
                    setThatFragment(contactUsFragment)
                    navview.visibility = View.GONE

                }
                R.id.nav_exit -> {
                    PreferencesManager.getDefaultSharedPreferences(binding.root.context).wipe()
                    fAuth.signOut()
                    startActivity(Intent(this, Initial::class.java ))
                    Intent(this, Initial::class.java)
                }
            }
            // Indica que el elemento ha sido seleccionado
            binding.navView.setCheckedItem(-1)
            menuItem.isChecked = true
            true
        }
        btnDeleteAccount.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete account")
            builder.setMessage("Are you sure you want to delete the account")
            builder.setPositiveButton("Accept") { dialog, which ->
                fStore.collection("users").document(email).delete()
                fAuth.currentUser?.delete()
                fStorage.child("img").child(email).delete()
                Toast.makeText(this, "The account has been deleted", Toast.LENGTH_LONG).show()
                PreferencesManager.getDefaultSharedPreferences(binding.root.context).wipe()
                startActivity(Intent(this, Initial::class.java ))
                Intent(binding.root.context, Initial::class.java)
            }
            builder.setNegativeButton("Cancel") { dialog, which ->}
            builder.show()
        }
    }

    private fun closeNav() {
        binding.navView.isVisible = false
        navview.visibility = View.GONE
        if (isnavview) {
            animateAndHideNavigationView(navview)
            isnavview = false
        }
        buttonsVisibility()
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

        private fun buttonsVisibility()
        {
            handler.postAtTime(Runnable
            {
                imageButton.visibility = View.VISIBLE
                transparentButton.visibility = View.GONE
            }, SystemClock.uptimeMillis() + 850)
        }

        fun animateAndHideNavigationView(navigationView: NavigationView) {
        val animation = TranslateAnimation(0f, -navigationView.width.toFloat(), 0f, 0f)
        animation.duration = 800
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                navigationView.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        navigationView.startAnimation(animation)
    }

}

