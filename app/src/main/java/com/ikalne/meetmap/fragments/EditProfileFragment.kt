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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
lateinit var binding: FragmentEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
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
       /* binding.btnDeleteAccount.setOnClickListener{
            MeetMapApplication.prefs.wipe()
            startActivity(Intent(this.requireContext(), Initial::class.java ))
            Intent(getActivity(), Initial::class.java)
        }
        binding.btnlogout.setOnClickListener{
            MeetMapApplication.prefs.wipe()
            startActivity(Intent(this.requireContext(), Login::class.java ))
            Intent(getActivity(), Login::class.java)

        }*/
//        binding.apply {
//            btnlogout.s
//        }
        Glide.with(this) //.load("https://images.unsplash.com/photo-1512849934327-1cf5bf8a5ccc?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80")
            .load(R.drawable.cara)
            .circleCrop()
            .into(binding.ivuser)

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}