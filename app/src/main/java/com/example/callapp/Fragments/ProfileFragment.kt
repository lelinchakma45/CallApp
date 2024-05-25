package com.example.callapp.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.callapp.AboutAppActivity
import com.example.callapp.UpdateInfoActivity
import com.example.callapp.ViewModel.UserProfileViewModel
import com.example.callapp.ViewModelFactory.UserProfileViewModelFactory
import com.example.callapp.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: UserProfileViewModel by viewModels {
        UserProfileViewModelFactory(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        binding.editBtn.setOnClickListener {
            intentUpdate()
        }
        binding.intentUpdate.setOnClickListener {
            intentUpdate()
        }
        binding.modeBtn.setOnToggledListener { _, isOn ->
            if (isOn) {
                // Enable dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Disable dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        binding.aboutAppBtn.setOnClickListener {
            startActivity(Intent(requireContext(),AboutAppActivity::class.java))
        }
        return binding.root
    }

    private fun intentUpdate(){
        val user = profileViewModel.user.value
        user?.let {
            val intent = Intent(requireContext(), UpdateInfoActivity::class.java).apply {
                putExtra("user_id",it.id)
                putExtra("user_name", it.name)
                putExtra("user_email", it.email)
                putExtra("user_mobile", it.mobile)
            }
            startActivity(intent)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                binding.nameShow.text = it.name
                binding.phoneShow.text = it.mobile
            }
        })
        profileViewModel.fetchUserById(1)
    }

}