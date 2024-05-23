package com.example.callapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.callapp.Adapter.TabAdapter
import com.example.callapp.databinding.FragmentCallBinding
import com.google.android.material.tabs.TabLayoutMediator

class CallFragment : Fragment() {
    private lateinit var binding: FragmentCallBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCallBinding.inflate(inflater, container, false)
//        checkAndRequestPermissions()
        setupViewPager()
//        binding.addBtn.setOnClickListener {
//            startActivity(Intent(requireContext(),AddContactActivity::class.java))
//        }
        return binding.root
    }
    private fun setupViewPager() {
        val adapter = TabAdapter(this)
        adapter.addFragment(RecentFragment(), "Recent")
        adapter.addFragment(ContactsFragment(), "Contacts")
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()
    }

//    private fun checkAndRequestPermissions() {
//        val permissions = arrayOf(
//            Manifest.permission.READ_CALL_LOG,
//            Manifest.permission.WRITE_CALL_LOG,
//            Manifest.permission.CALL_PHONE
//        )
//
//        val permissionsToRequest = permissions.filter {
//            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
//        }
//
//        if (permissionsToRequest.isNotEmpty()) {
//            ActivityCompat.requestPermissions(requireActivity(), permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
//        } else {
//            showCallRecentHistory()
//        }
//    }
//
//    private fun showCallRecentHistory() {
//        val callLogList = mutableListOf<CallLogData>()
//        val cursor = requireContext().contentResolver.query(
//            CallLog.Calls.CONTENT_URI,
//            null,
//            null,
//            null,
//            "${CallLog.Calls.DATE} DESC" // Order by most recent first
//        )
//
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                val name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME)) ?: "Unknown"
//                val number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
//                val date = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))
//                val type = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE))
//                callLogList.add(CallLogData(name, number, date, type))
//            } while (cursor.moveToNext())
//            cursor.close()
//        }
//
//        binding.callRecentHistory.layoutManager = LinearLayoutManager(requireContext())
//        binding.callRecentHistory.adapter = CallHistoryAdapter(callLogList)
//    }
//
//    companion object {
//        private const val PERMISSION_REQUEST_CODE = 100
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if ((grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED })) {
//                showCallRecentHistory()
//            } else {
//                // Permission denied, show a message to the user.
//                Toast.makeText(requireContext(), "Permissions required to access call logs", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}
