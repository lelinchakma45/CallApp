package com.example.callapp.Fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.callapp.Adapter.SmsAdapter
import com.example.callapp.Data.SmsData
import com.example.callapp.SmsDetailsActivity
import com.example.callapp.databinding.FragmentSmsBinding

class SmsFragment : Fragment() {
    private val PERMISSIONS_REQUEST_CODE = 123
    private lateinit var binding: FragmentSmsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSmsBinding.inflate(layoutInflater)
        checkAndRequestPermissions()
        return binding.root
    }
    private fun checkAndRequestPermissions() {
        val permissionsNeeded = mutableListOf<String>()

        context?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_SMS)
            }
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_CONTACTS)
            }
        }

        if (permissionsNeeded.isNotEmpty()) {
            activity?.let {
                ActivityCompat.requestPermissions(it, permissionsNeeded.toTypedArray(), PERMISSIONS_REQUEST_CODE)
            }
        } else {
            loadSmsMessages()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                loadSmsMessages()
            } else {
                context?.let {
                    Toast.makeText(it, "Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadSmsMessages() {
        val smsList = mutableListOf<SmsData>()
        val uri = Uri.parse("content://sms/inbox")
        val cursor = context?.contentResolver?.query(uri, null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                val body = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                smsList.add(SmsData(address, body))
            } while (cursor.moveToNext())
            cursor.close()
        }

        setupRecyclerView(smsList)
    }

    private fun setupRecyclerView(smsList: List<SmsData>) {
        binding.smsHistoryList.layoutManager = LinearLayoutManager(requireContext())
        binding.smsHistoryList.adapter = SmsAdapter(smsList, object : SmsAdapter.OnItemClickListener {
            override fun onItemClick(sms: SmsData) {
                val intent = Intent(requireContext(), SmsDetailsActivity::class.java)
                intent.putExtra("contact_address", sms.address)
                startActivity(intent)
            }
        })

    }
}