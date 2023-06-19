package com.ikalne.meetmap

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter


class SliderAdapter(var context: Context, sliderModalArrayList: ArrayList<SliderModal>) :
    PagerAdapter() {
    // creating variables for layout
    // inflater, context and array list.
    var layoutInflater: LayoutInflater? = null
    var sliderModalArrayList: ArrayList<SliderModal>

    // creating constructor.
    init {
        this.sliderModalArrayList = sliderModalArrayList
    }

    override fun getCount(): Int {
        // inside get count method returning
        // the size of our array list.
        return sliderModalArrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        // inside isViewFromobject method we are
        // returning our Relative layout object.
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        // in this method we will initialize all our layout
        // items and inflate our layout file as well.
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // below line is use to inflate the
        // layout file which we created.
        val view: View = layoutInflater!!.inflate(R.layout.slider_layout, container, false)

        // initializing our views.
        val titleTV = view.findViewById<TextView>(R.id.idTVtitle)
        val sliderRL = view.findViewById<RelativeLayout>(R.id.idRLSlider)

        // setting data to our views.
        val modal: SliderModal = sliderModalArrayList[position]
        titleTV.setText(modal.title)

        // after setting the data to our views we
        // are adding the view to our container.
        container.addView(view)

        // at last we are
        // returning the view.
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // this is a destroy view method
        // which is use to remove a view.
        container.removeView(`object` as RelativeLayout)
    }
}