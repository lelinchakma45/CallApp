package com.example.callapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.callapp.Model.PhoneLogin
import com.example.callapp.ViewModel.UserProfileViewModel
import com.example.callapp.ViewModelFactory.UserProfileViewModelFactory
import com.example.callapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val profileViewModel: UserProfileViewModel by viewModels {
        UserProfileViewModelFactory(application)
    }
    override fun onStart() {
        super.onStart()
        val currentUserId = 1
        profileViewModel.user.observe(this, Observer { user ->
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
        profileViewModel.fetchUserById(currentUserId)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signEmailBtn.setOnClickListener {
            val name = binding.userNameEt.text.toString()
            val email = binding.emailEt.text.toString()
            val mobile = binding.phoneEt.text.toString()
            val pass = binding.passEt.text.toString()

            val userProfile = PhoneLogin(name = name, email = email, pass = pass, mobile = mobile)
            profileViewModel.insertUserProfile(userProfile)
        }
        profileViewModel.insertionStatus.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "User profile inserted successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
            } else {
                Toast.makeText(this, "Failed to insert user profile", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
