package com.example.callapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.callapp.ViewModel.UserProfileViewModel
import com.example.callapp.ViewModelFactory.UserProfileViewModelFactory
import com.example.callapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val profileViewModel: UserProfileViewModel by viewModels {
        UserProfileViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val mobile = binding.phoneEt.text.toString()
            val pass = binding.passEt.text.toString()

            if (mobile.isBlank() || pass.isBlank()) {
                Toast.makeText(this, "Please enter both mobile and password", Toast.LENGTH_SHORT).show()
            } else {
                profileViewModel.getUserProfiles().observe(this, Observer { userProfiles ->
                    val user = userProfiles.find { it.mobile == mobile && it.pass == pass }
                    if (user != null) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Invalid mobile or password", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        binding.registerBtnText.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}