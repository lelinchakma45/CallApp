package com.example.callapp

import android.Manifest
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.callapp.Adapter.SmsDetailsAdapter
import com.example.callapp.Data.SmsDetailsData
import com.example.callapp.databinding.ActivitySmsDetailsBinding

class SmsDetailsActivity : AppCompatActivity() {
    private lateinit var adapter: SmsDetailsAdapter
    private lateinit var contactAddress: String
    private lateinit var binding: ActivitySmsDetailsBinding

    companion object {
        private const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
        private const val PERMISSIONS_REQUEST_READ_SMS = 101
        private const val PERMISSIONS_REQUEST_SEND_SMS = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySmsDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        contactAddress = intent.getStringExtra("contact_address") ?: ""

        // Check for READ_CONTACTS permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS)
        } else {
            displayContactName()
        }

        // Check for READ_SMS permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS), PERMISSIONS_REQUEST_READ_SMS)
        } else {
            loadMessages()
        }

        // Check for SEND_SMS permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), PERMISSIONS_REQUEST_SEND_SMS)
        }

        binding.sendButton.setOnClickListener {
            val message = binding.inputText.text.toString()
            val phoneNumber= contactAddress
            if (message.isNotEmpty()){
                sendSms(phoneNumber,message)
            }
            else{
                Toast.makeText(this,"Fill Message Field",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    displayContactName()
                } else {
                    binding.incomingName.text = contactAddress
                }
            }
            PERMISSIONS_REQUEST_READ_SMS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadMessages()
                } else {
                    // Handle the case where permission is not granted
                }
            }
        }
    }

    private fun displayContactName() {
        val contactName = getContactName(contactAddress)
        binding.incomingName.text = contactName ?: contactAddress
    }

    private fun getContactName(contactAddress: String): String? {
        val contentResolver: ContentResolver = contentResolver
        val uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI
        val projection = arrayOf(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME)
        val selection = "${ContactsContract.CommonDataKinds.Email.ADDRESS} = ?"
        val selectionArgs = arrayOf(contactAddress)

        var contactName: String? = null
        val cursor: Cursor? = contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor?.use {
            if (it.moveToFirst()) {
                contactName = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME))
            }
        }
        return contactName
    }

    private fun loadMessages() {
        val smsList = mutableListOf<SmsDetailsData>()
        val uri = Telephony.Sms.CONTENT_URI
        val selection = "${Telephony.Sms.ADDRESS} = ?"
        val selectionArgs = arrayOf(contactAddress)
        val cursor = contentResolver.query(uri, null, selection, selectionArgs, null)

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val address = it.getString(it.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val body = it.getString(it.getColumnIndexOrThrow(Telephony.Sms.BODY))
                    val type = it.getInt(it.getColumnIndexOrThrow(Telephony.Sms.TYPE))
                    val isIncoming = type == Telephony.Sms.MESSAGE_TYPE_INBOX
                    smsList.add(SmsDetailsData(address, body, isIncoming))
                } while (it.moveToNext())
            }
        }

        smsList.reverse()

        adapter = SmsDetailsAdapter(smsList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        Log.d("SmsDetailsActivity", "Loaded ${smsList.size} messages for $contactAddress")
    }

    private fun sendSms(phoneNumber: String, message: String) {
        val sentIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent("SMS_SENT").putExtra("phoneNumber", phoneNumber),
            PendingIntent.FLAG_IMMUTABLE
        )

        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, sentIntent, null)
            Toast.makeText(this, "Message Sent", Toast.LENGTH_LONG).show()
            // Clear input field after sending the message
            binding.inputText.text?.clear()
            // Load latest messages
            loadMessages()
        } catch (e: SecurityException) {
            // Handle permission-related errors
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SEND_SMS),
                PERMISSIONS_REQUEST_SEND_SMS
            )
        } catch (e: Exception) {
            // Handle other errors
            Toast.makeText(
                this,
                "Error sending message: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
            Log.e("SmsDetailsActivity", "Error sending message: ${e.message}")
        }
    }

}
