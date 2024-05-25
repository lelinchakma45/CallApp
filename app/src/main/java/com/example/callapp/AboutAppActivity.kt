package com.example.callapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.callapp.databinding.ActivityAboutAppBinding

class AboutAppActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAboutAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        binding.ratingBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/"))
            startActivity(intent)
        }
        setContentView(binding.root)
    }
}