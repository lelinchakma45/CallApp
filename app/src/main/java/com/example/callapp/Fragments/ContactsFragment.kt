package com.example.callapp.Fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.callapp.Adapter.ContactsAdapter
import com.example.callapp.Data.ContactData
import com.example.callapp.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    private val ADD_CONTACT_REQUEST_CODE = 101 // Define a request code for adding a contact
    private lateinit var binding: FragmentContactsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       binding = FragmentContactsBinding.inflate(layoutInflater)
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS)
        } else {
            loadContacts()
        }
        binding.addBtn.setOnClickListener {
            startActivity(Intent(requireContext(), AddContactActivity::class.java))
        }
        return binding.root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_CONTACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Handle the result of adding a contact here
            loadContacts() // Refresh the contacts list
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                loadContacts()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadContacts() {
        val contactsList = mutableListOf<ContactData>()
        val cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contactsList.add(ContactData(name, phoneNumber))
            } while (cursor.moveToNext())
            cursor.close()
        }
        contactsList.sortBy { it.name }

        binding.contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.contactsRecyclerView.adapter = ContactsAdapter(contactsList)
    }
}