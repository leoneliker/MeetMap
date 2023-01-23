package com.ikalne.meetmap.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ikalne.meetmap.Initial
import com.ikalne.meetmap.Login
import com.ikalne.meetmap.MeetMapApplication
import com.ikalne.meetmap.R
import com.ikalne.meetmap.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {
lateinit var binding: FragmentEditProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container,false)
        initUI()

        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }
    fun initUI()
    {

        binding.btnDeleteAccount.setOnClickListener{
            MeetMapApplication.prefs.wipe()
//            initialActivity()
        }
        binding.btnlogout.setOnClickListener{
            MeetMapApplication.prefs.wipe()
//            loginActivity()

        }
//        binding.apply {
//            btnlogout.s
//        }
    }

//    fun loginActivity()
//    {
//        startActivity(Intent(this, Login::class.java ))
//    }
//    fun initialActivity()
//    {
//        startActivity(Intent(this, Initial::class.java ))
//    }
}