package com.ikalne.meetmap.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.ikalne.meetmap.R
import com.ikalne.meetmap.databinding.FragmentConctactUsBinding


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
            .load(R.drawable.dani_meetmap)
            .circleCrop()
            .into(binding.ivDani)

        Glide.with(this)
            .load(R.drawable.iker_meetmap)
            .circleCrop()
            .into(binding.ivIker)

        Glide.with(this)
            .load(R.drawable.nerea_meetmap)
            .circleCrop()
            .into(binding.ivNerea)

        fun createStyledLinkText(linkText: String, color: Int): SpannableString {
            val spannableString = SpannableString(linkText)
            spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), color)), 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            //spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannableString
        }

        val spannableStringAlmu = createStyledLinkText("AlmuFerCar", R.color.secondary)
        binding.tvGitAlmulink.text = spannableStringAlmu
        binding.tvGitAlmulink.movementMethod = LinkMovementMethod.getInstance()
        binding.tvGitAlmulink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/AlmuFerCar"))
            startActivity(intent)
        }

        val spannableStringDani = createStyledLinkText("Daniel García", R.color.secondary)
        binding.tvGitDanilink.text = spannableStringDani
        binding.tvGitDanilink.movementMethod = LinkMovementMethod.getInstance()
        binding.tvGitDanilink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/fregonaespanola"))
            startActivity(intent)
        }

        val spannableStringIker = createStyledLinkText("leoneliker", R.color.secondary)
        binding.tvGitIkerlink.text = spannableStringIker
        binding.tvGitIkerlink.movementMethod = LinkMovementMethod.getInstance()
        binding.tvGitIkerlink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/leoneliker"))
            startActivity(intent)
        }

        val spannableStringNerea = createStyledLinkText("Nereare4", R.color.secondary)
        binding.tvGitNerealink.text = spannableStringNerea
        binding.tvGitNerealink.movementMethod = LinkMovementMethod.getInstance()
        binding.tvGitNerealink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Nereare4"))
            startActivity(intent)
        }

        //binding.tvGitAlmulink.text = Html.fromHtml("<a href=\"https://github.com/AlmuFerCar\">AlmuFerCar</a>")
        //binding.tvGitAlmulink.movementMethod = LinkMovementMethod.getInstance()
//        binding.tvGitIkerlink.text = Html.fromHtml("<a href=\"https://github.com/leoneliker\">leoneliker</a>")
//        binding.tvGitIkerlink.movementMethod = LinkMovementMethod.getInstance()
//        binding.tvGitNerealink.text = Html.fromHtml("<a href=\"https://github.com/Nereare4\">Nereare4</a>")
//        binding.tvGitNerealink.movementMethod = LinkMovementMethod.getInstance()
    }


}
