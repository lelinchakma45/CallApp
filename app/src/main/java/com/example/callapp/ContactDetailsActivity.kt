package com.example.callapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.callapp.Adapter.CallSingleDetailsAdapter
import com.example.callapp.Utils.LogCallUtils
import com.example.callapp.databinding.ActivityContactDetailsBinding
import java.util.Locale

class ContactDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityContactDetailsBinding

    private val CALL_PHONE_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityContactDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        loadContactDetails()
        initValues()

        setContentView(binding.root)
    }

    fun String.firstLetterUpperCase(): String {
        return if (this.isNotEmpty()) this[0].uppercase(Locale.getDefault()) else ""
    }

    private fun loadContactDetails() {
        val contactName = intent.getStringExtra("CONTACT_NAME")
        val contactPhone = intent.getStringExtra("CONTACT_PHONE")
        binding.incomingName.text = contactName
        if (!contactName.isNullOrEmpty()) {
            binding.incomingName.text = contactName
            binding.nameView.text = contactName
            binding.firstLetter.text = contactName.firstLetterUpperCase()
        } else {
            binding.incomingName.text = contactPhone
            binding.nameView.text = contactPhone
            binding.firstLetter.text = "UN" // Or any default value you prefer
        }
        binding.nameView.text = contactName
        binding.phoneShow.text = contactPhone

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.callBtn.setOnClickListener {
            callRequest(contactPhone)
        }
        binding.callBtn2.setOnClickListener {
            callRequest(contactPhone)
        }
        binding.callBtn3.setOnClickListener {
            callRequest(contactPhone)
        }
        binding.contactSetting.setOnClickListener {
            val intent = Intent(this, UpdateActivity::class.java)
            // Pass any necessary data to the UpdateActivity, such as contact name or ID
             intent.putExtra("contactNumber", contactPhone)
            startActivity(intent)
        }
    }

    private fun callRequest(contactPhone: String?) {
        contactPhone?.let { phone ->
            val context = binding.root.context
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phone")
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                context.startActivity(intent)
            } else {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    CALL_PHONE_REQUEST_CODE
                )
            }
        }
    }
    private fun initValues() {
        val contactName = intent?.getStringExtra("CONTACT_NAME")
        val contactPhone = intent?.getStringExtra("CONTACT_PHONE")
        if (contactPhone == null) {
            finish()
            return
        }

        val callLogUtils = LogCallUtils.getInstance(applicationContext)

        val callLogs = callLogUtils.getAllCallLogs(contactPhone)

        val adapter = CallSingleDetailsAdapter(callLogs)
        binding.recyclerViewCallLogs.adapter = adapter
        binding.recyclerViewCallLogs.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCallLogs.isNestedScrollingEnabled = false
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }


}