package com.ikalne.meetmap.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        clearFields()
        binding.btnsend.setOnClickListener {
            if (!binding.email.text.contains("@")) {
                showError(binding.etemail, "Email is not valid")
            }
            else {
                Toast.makeText(requireActivity(), "Form submitted successfully", Toast.LENGTH_LONG)
                .show()
                changeActivity()
            }
        }
        binding.btncancel.setOnClickListener {
            changeActivity()
        }
    }

    fun changeActivity()
    {
        val intent = Intent(requireActivity(), MainAppActivity::class.java)
        startActivity(intent)
    }
    fun clearFields()
    {
        binding.nombre.text.clear()
        binding.email.text.clear()
        binding.suggestion.text.clear()
    }

    private fun showError(textInputLayout: TextInputLayout, error: String) {
        textInputLayout.error = error
    }
}
