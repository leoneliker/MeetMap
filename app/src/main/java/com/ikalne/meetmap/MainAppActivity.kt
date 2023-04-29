package com.ikalne.meetmap

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
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
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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
    private lateinit var navView: NavigationView
    lateinit var fStorage: StorageReference
    private val handler = Handler()
    private var isnavview = false
    private lateinit var navview: NavigationView
    private var isFragmentLoaded = false
    private val mapFragment = MapFragment()
    private var noInternetDialog: AlertDialog? = null
    private var retryCount = 0


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
        frame = binding.frame
        navView = binding.navView


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
        val username = headerView.findViewById<TextView>(R.id.username)
        val emailuser = headerView.findViewById<TextView>(R.id.email)
        val btnDeleteAccount = navview.findViewById<Button>(R.id.btnDeleteAccount)
        val slidein = AnimationUtils.loadAnimation(this, R.anim.slidein)



        //email = PreferencesManager.getDefaultSharedPreferences(this).getEmail()
        transparentButton.visibility = View.GONE

        setThatFragment(mapFragment)

        email = PreferencesManager.getDefaultSharedPreferences(binding.root.context).getEmail()
        emailuser.setText(email)

        fStore.collection("users").document(email).get().addOnSuccessListener {
            username.setText(it.get("name") as String)
            //binding.mail.setText(email)
            Glide.with(this)
                .load(it.get("img") as String)
                .circleCrop()
                .into(imagenav)
        }

        fStore.collection("users").document(email).addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                // Actualiza la vista con el nuevo valor del nombre
                username.setText(documentSnapshot.getString("name"))
                Glide.with(this)
                    .load(documentSnapshot.getString("img"))
                    .circleCrop()
                    .into(imagenav)
            }
        }

        bottomNavView.setSelectedItemId(R.id.house)

        imageButton.setOnClickListener()
        {
            binding.navView.isVisible = true
            isnavview = true
            val currentFocus = this.currentFocus
            if (currentFocus != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
            }
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
                /*R.id.nav_notifications -> {
                    closeNav()
                    setThatFragment(notificationsFragment)
                }*/
                R.id.nav_manusu -> {
                    closeNav()
                    setThatFragment(faqsFragment)
                }
                R.id.contactus -> {
                    closeNav()
                    setThatFragment(contactUsFragment)
                }
                R.id.nav_exit -> {
                    PreferencesManager.getDefaultSharedPreferences(binding.root.context).wipe()
                    fAuth.signOut()
                    startActivity(Intent(this, Initial::class.java))
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
            builder.setTitle(resources.getString(R.string.deleteaccount))
            builder.setMessage(resources.getString(R.string.deleteAccountQuestion))
            builder.setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                fStore.collection("users").document(email).delete()
                fAuth.currentUser?.delete()
                fStorage.child("img").child(email).delete()
                Toast.makeText(
                    this,
                    resources.getString(R.string.deleteAccountSuccesful),
                    Toast.LENGTH_LONG
                ).show()
                PreferencesManager.getDefaultSharedPreferences(binding.root.context).wipe()
                startActivity(Intent(this, Initial::class.java))
                Intent(binding.root.context, Initial::class.java)
            }
            builder.setNegativeButton(resources.getString(R.string.cancel)) { dialog, which -> }
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
        Toast.makeText(this, resources.getString(R.string.backAgain), Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    private fun setThatFragment(fragment: Fragment) {
        if (isConnectedToInternet()) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame, fragment)
                commit()
            }
        } else {
            showNoInternetAlert()
        }

    }


    private fun buttonsVisibility() {
        handler.postAtTime(Runnable
        {
            imageButton.visibility = View.VISIBLE
            transparentButton.visibility = View.GONE
        }, SystemClock.uptimeMillis() + 850
        )
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

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (!isConnectedToInternet()) {
                showNoInternetAlert()
            } else {
                if (!isFragmentLoaded) {
                    loadFragment()
                }
            }
        }
    }
    private fun loadFragment() {
        // Intenta cargar el fragmento aquí
        setThatFragment(mapFragment)
        hideNoInternetAlert()
        isFragmentLoaded = true
    }
    private fun hideNoInternetAlert() {
        noInternetDialog?.dismiss()
        noInternetDialog = null
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showNoInternetAlert() {
        if (noInternetDialog?.isShowing == true) {
            noInternetDialog?.dismiss()
        }

        val builder = AlertDialog.Builder(this)
        // Modificamos el texto del botón de "Reintentar"
        val buttonText = if (retryCount >= 2) {
            builder.setTitle(getString(R.string.nointernetitleexit))
            builder.setMessage(getString(R.string.nointernetsubtitleexit))
            getString(R.string.exitalert)
        } else {
            builder.setTitle(getString(R.string.nointernetitle))
            builder.setMessage(getString(R.string.nointernetsubtitle))
            getString(R.string.nointernetbtn)
        }
        builder.setPositiveButton(buttonText) { dialog, which ->
            if (!isConnectedToInternet()) {
                // Incrementamos el contador de reintentos
                retryCount++
                showNoInternetAlert()
            } else {
                // Reiniciamos el contador de reintentos
                retryCount = 0
                hideNoInternetAlert()
            }
            // Si se ha presionado el botón dos o más veces, cerramos la app
            if (retryCount > 2) {
                hideNoInternetAlert()
                finishAffinity()
            }
        }

        builder.setCancelable(false)
        noInternetDialog = builder.create()
        noInternetDialog?.show()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkReceiver)
    }
}

