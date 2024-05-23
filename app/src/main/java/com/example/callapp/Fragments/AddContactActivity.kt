package com.example.callapp.Fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.callapp.Utils.ContactManager
import com.example.callapp.R

class AddContactActivity : AppCompatActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var addButton: Button

    private lateinit var contactManager: ContactManager

    companion object {
        private const val PERMISSION_REQUEST_WRITE_CONTACTS = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        firstNameEditText = findViewById(R.id.editTextFirstName)
        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber)
        addButton = findViewById(R.id.buttonAdd)

        contactManager = ContactManager(this)

        addButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val phoneNumber = phoneNumberEditText.text.toString()

            if (checkPermissions()) {
                addContact(firstName, phoneNumber) // Pass both firstName and phoneNumber
            } else {
                requestPermissions()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_CONTACTS),
            PERMISSION_REQUEST_WRITE_CONTACTS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_WRITE_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val firstName = firstNameEditText.text.toString()
                    val phoneNumber = phoneNumberEditText.text.toString()
                    addContact(firstName, phoneNumber)
                } else {
                    Toast.makeText(
                        this,
                        "Permission denied, cannot add contact",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun addContact(firstName: String, phoneNumber: String) {
        val success = contactManager.addContact(firstName, phoneNumber)
        if (success) {
            Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK) // Set the result to indicate success
        } else {
            Toast.makeText(this, "Failed to add contact", Toast.LENGTH_SHORT).show()
        }
        finish() // Finish the activity
    }
}
