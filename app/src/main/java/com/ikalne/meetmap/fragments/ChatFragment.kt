package com.ikalne.meetmap.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ikalne.meetmap.ChatActivity
import com.ikalne.meetmap.ListOfChatsActivity
import com.ikalne.meetmap.R
import com.ikalne.meetmap.databinding.FragmentChatBinding
import com.ikalne.meetmap.databinding.FragmentConctactUsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        initUI()

        return binding.root
    }

    fun initUI() {
        binding.openChat.setOnClickListener{
            val intent = Intent(activity, ListOfChatsActivity::class.java)
            startActivity(intent)
        }
    }
}