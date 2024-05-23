package com.example.callapp

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateActivity : AppCompatActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var updateButton: Button

    private lateinit var contactNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        // Retrieve contact number passed from ContactDetailsActivity
        contactNumber = intent.getStringExtra("contactNumber") ?: ""

        firstNameEditText = findViewById(R.id.editTextFirstName)
        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber)
        updateButton = findViewById(R.id.buttonAdd)

        // Fill the phone number field with the provided contact number
        phoneNumberEditText.setText(contactNumber)

        // Retrieve contact name based on the provided contact number
        val contactName = getContactName(contactNumber)
        if (!contactName.isNullOrBlank()) {
            // Fill the name fields with the retrieved contact name
            val names = contactName.split(" ")
            if (names.isNotEmpty()) {
                firstNameEditText.setText(names[0])
                if (names.size > 1) {
                    lastNameEditText.setText(names.subList(1, names.size).joinToString(" "))
                }
            }
        }

        updateButton.setOnClickListener {
            // Implement the update contact logic
            updateContact()
        }
    }

    // Function to retrieve contact name based on contact number
    @SuppressLint("Range")
    private fun getContactName(phoneNumber: String): String? {
        val uri: Uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
        var contactName: String? = null
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                contactName = it.getString(it.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
            }
        }
        return contactName
    }

    // Function to update the contact
    private fun updateContact() {
        // Retrieve updated information from EditText fields
        val updatedFirstName = firstNameEditText.text.toString()
        val updatedPhoneNumber = phoneNumberEditText.text.toString()

        // Create content values with updated contact details
        val contentValues = ContentValues().apply {
            put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, updatedFirstName)
        }

        // Define the selection and selectionArgs to identify the contact to update
        val selection =
            "${ContactsContract.CommonDataKinds.Phone.NUMBER} = ?"
        val selectionArgs = arrayOf(updatedPhoneNumber)

        // Update the contact using the ContentResolver
        val resolver: ContentResolver = contentResolver
        val rowsAffected = resolver.update(ContactsContract.Data.CONTENT_URI, contentValues, selection, selectionArgs)

        if (rowsAffected > 0) {
            Toast.makeText(this, "Contact updated successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Failed to update contact", Toast.LENGTH_SHORT).show()
        }
    }
}
