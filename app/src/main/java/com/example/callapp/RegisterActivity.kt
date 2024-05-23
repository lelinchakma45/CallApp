package com.example.callapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.callapp.Model.PhoneLogin
import com.example.callapp.ViewModel.UserProfileViewModel
import com.example.callapp.ViewModelFactory.UserProfileViewModelFactory
import com.example.callapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var profileViewModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = UserProfileViewModelFactory(application)
        profileViewModel = ViewModelProvider(this, viewModelFactory).get(UserProfileViewModel::class.java)

        binding.signEmailBtn.setOnClickListener {
            val name = binding.userNameEt.text.toString()
            val email = binding.emailEt.text.toString()
            val mobile = binding.phoneEt.text.toString()
            val pass = binding.passEt.text.toString()

            val userProfile = PhoneLogin(name = name, email = email, pass = pass, mobile = mobile)
            profileViewModel.insertUserProfile(userProfile)

            finish()
        }

        binding.loginBtnText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
