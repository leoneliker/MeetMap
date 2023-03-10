package com.ikalne.meetmap.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.Html
import android.text.TextUtils.replace
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.ikalne.meetmap.MainAppActivity
import com.ikalne.meetmap.R
import com.ikalne.meetmap.databinding.FragmentConctactUsBinding
import com.ikalne.meetmap.databinding.FragmentEditProfileBinding


class ConctactUsFragment : Fragment() {
    lateinit var binding: FragmentConctactUsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConctactUsBinding.inflate(inflater, container, false)
        initUI()

        return binding.root
    }

    fun initUI() {
        Glide.with(this)
            .load(R.drawable.almu_meetmap)
            .circleCrop()
            .into(binding.ivAlmu)

        Glide.with(this)
            .load(R.drawable.nerea_meetmap)
            .circleCrop()
            .into(binding.ivNerea)

        binding.tvGitAlmulink.text = Html.fromHtml("<a href=\"https://github.com/AlmuFerCar\">AlmuFerCar</a>")
        binding.tvGitAlmulink.movementMethod = LinkMovementMethod.getInstance()
    }


}
