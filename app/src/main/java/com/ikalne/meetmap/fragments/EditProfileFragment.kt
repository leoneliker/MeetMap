package com.ikalne.meetmap.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
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

        return binding.root
    }
    fun initUI()
    {
        //binding.email.setEnabled(false)
        binding.btnDeleteAccount.setOnClickListener{
            MeetMapApplication.prefs.wipe()
            startActivity(Intent(this.requireContext(), Initial::class.java ))
            Intent(getActivity(), Initial::class.java)
        }
        binding.btnlogout.setOnClickListener{
            MeetMapApplication.prefs.wipe()
            startActivity(Intent(this.requireContext(), Login::class.java ))
            Intent(getActivity(), Login::class.java)

        }
//        binding.apply {
//            btnlogout.s
//        }
        Glide.with(this) //.load("https://images.unsplash.com/photo-1512849934327-1cf5bf8a5ccc?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80")
            .load(R.drawable.cara)
            .circleCrop()
            .into(binding.ivuser)

    }


}