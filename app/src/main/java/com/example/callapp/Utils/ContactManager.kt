package com.example.callapp.Utils

import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract

class ContactManager(private val context: Context) {

    fun addContact(firstName: String, phoneNumber: String): Boolean {
        // Set the display name as the first name
        val displayName = firstName

        // Create a ContentValues object to insert raw contact data
        val contentValues = ContentValues().apply {
            putNull(ContactsContract.RawContacts.ACCOUNT_TYPE)
            putNull(ContactsContract.RawContacts.ACCOUNT_NAME)
        }

        // Insert the raw contact and get the inserted raw contact URI
        val rawContactUri = context.contentResolver.insert(
            ContactsContract.RawContacts.CONTENT_URI,
            contentValues
        ) ?: return false

        // Extract the raw contact ID from the URI
        val rawContactId = rawContactUri.lastPathSegment?.toLongOrNull() ?: return false

        // Create ContentValues for the phone number
        val phoneContentValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
            put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
        }

        // Insert the phone number data
        context.contentResolver.insert(
            ContactsContract.Data.CONTENT_URI,
            phoneContentValues
        )

        // Create ContentValues for the display name
        val nameContentValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName)
        }

        // Insert the display name data
        context.contentResolver.insert(
            ContactsContract.Data.CONTENT_URI,
            nameContentValues
        )

        // Return true to indicate success
        return true
    }
}
