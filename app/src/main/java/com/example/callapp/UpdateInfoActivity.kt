package com.example.callapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.callapp.Fragments.ProfileFragment
import com.example.callapp.Model.PhoneLogin
import com.example.callapp.ViewModel.UserProfileViewModel
import com.example.callapp.databinding.ActivityUpdateInfoBinding

class UpdateInfoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUpdateInfoBinding
    private lateinit var viewModel: UserProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateInfoBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)
        super.onCreate(savedInstanceState)
        val userId = intent.getIntExtra("user_id", 0) // Default value 0
        viewModel.fetchUserById(userId)
        val userName = intent.getStringExtra("user_name")
        val userEmail = intent.getStringExtra("user_email")
        val userMobile = intent.getStringExtra("user_mobile")

        binding.userNameEt.setText(userName)
        binding.emailEt.setText(userEmail)
        binding.phoneEt.setText(userMobile)

        binding.updateBtn.setOnClickListener {
            val updatedName = binding.userNameEt.text.toString()
            val updatedEmail = binding.emailEt.text.toString()
            val updatedMobile = binding.phoneEt.text.toString()

            val updatedProfile = viewModel.user.value?.copy(name = updatedName, email = updatedEmail, mobile = updatedMobile)
            if (updatedProfile != null) {
                viewModel.updateUserProfile(updatedProfile)

                viewModel.insertionStatus.observe(this, Observer { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this,"Failed to update",Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Handle error, user not found or other condition
            }
            startActivity(Intent(this, MainActivity::class.java).apply {
                putExtra("navigate_to_profile", true)
            })
        }
        setContentView(binding.root)
    }

}