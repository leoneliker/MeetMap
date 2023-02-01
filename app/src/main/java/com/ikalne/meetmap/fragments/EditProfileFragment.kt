package com.ikalne.meetmap.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ikalne.meetmap.Initial
import com.ikalne.meetmap.Login
import com.ikalne.meetmap.PreferencesManager
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
        binding.btnDeleteAccount.setOnClickListener{
            PreferencesManager.getDefaultSharedPreferences(binding.root.context).wipe()
            startActivity(Intent(this.requireContext(), Initial::class.java ))
            Intent(getActivity(), Initial::class.java)
        }
        binding.btnlogout.setOnClickListener{
            PreferencesManager.getDefaultSharedPreferences(binding.root.context).wipe()
            startActivity(Intent(this.requireContext(), Login::class.java ))
            Intent(getActivity(), Login::class.java)

        }
//        binding.apply {
//            btnlogout.s
//        }
    }


}