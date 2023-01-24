package com.ikalne.meetmap

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager.widget.ViewPager


class Initial : AppCompatActivity() {
    lateinit var dotsLL: LinearLayout
    var adapter: SliderAdapter? = null
    private lateinit var dots: MutableList<TextView>
    var size = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        val botonC = findViewById<Button>(R.id.create)
        val botonL = findViewById<Button>(R.id.login)
        val viewPager = findViewById<ViewPager>(R.id.idViewPager)
        dotsLL = findViewById<LinearLayout>(R.id.idLLDots)

        var sliderModalArrayList: ArrayList<SliderModal> = arrayListOf()
        sliderModalArrayList.add(SliderModal(getString(R.string.slide1)))
        sliderModalArrayList.add(SliderModal(getString(R.string.slide2)))
        sliderModalArrayList.add(SliderModal(getString(R.string.slide3)))
        sliderModalArrayList.add(SliderModal(getString(R.string.slide4)))
        adapter = SliderAdapter(this@Initial, sliderModalArrayList)
        viewPager.adapter = adapter
        viewPager.autoScroll(3000)

        size = sliderModalArrayList.size
        addDots(size, 0)


        botonC.setOnClickListener(OnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        })
        botonL.setOnClickListener(OnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        })
    }

    fun checkUserValues()
    {
        if (MeetMapApplication.prefs.getEmail().isNotEmpty())
        {
            startActivity(Intent(this, MainAppActivity::class.java))
        }
    }
    private fun addDots(size: Int, pos: Int) {
        dots = mutableListOf()
        dotsLL.removeAllViews()
        for (i in 0 until size) {
            dots.add(TextView(this))
            dots[i].text = Html.fromHtml("•")
            dots[i].textSize = 35f
            dots.get(i).setTextColor(resources.getColor(R.color.dark_gray))
            dotsLL.addView(dots[i])
        }
        if (dots.size > 0) {
            dots.get(pos).setTextColor(resources.getColor(R.color.secondary))
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


    fun ViewPager.autoScroll(interval: Long) {
        val handler = Handler()
        var scrollPosition = 0
        val runnable = object : Runnable{
            override fun run(){
                val count = adapter?.count ?:0
                setCurrentItem(scrollPosition++% count, true)
                handler.postDelayed(this,interval)
            }
        }
        addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {
                scrollPosition=position+1
                addDots(size, position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        handler.post(runnable)


    }
}