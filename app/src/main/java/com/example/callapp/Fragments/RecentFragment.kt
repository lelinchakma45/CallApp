package com.example.callapp.Fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.callapp.Adapter.CallLogAdapter
import com.example.callapp.Utils.CallLogUtils
import com.example.callapp.ContactDetailsActivity
import com.example.callapp.Data.CallLogInfo
import com.example.callapp.databinding.FragmentRecentBinding

class RecentFragment : Fragment() {
    private lateinit var binding: FragmentRecentBinding
    private lateinit var adapter: CallLogAdapter
    private lateinit var onItemClickListener: CallLogAdapter.OnCallLogItemClickListener

    companion object {
        private const val REQUEST_CODE_READ_CALL_LOG = 1
    }

    @Nullable
    override fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = FragmentRecentBinding.inflate(layoutInflater)
        initComponents()
        return binding.root
    }

    private fun initComponents() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_CALL_LOG), REQUEST_CODE_READ_CALL_LOG)
        } else {
            setupRecyclerView()
            loadData()
        }
    }

    private fun setupRecyclerView() {
        val mLayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = mLayoutManager
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = CallLogAdapter(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun loadData() {
        val callLogUtils = CallLogUtils.getInstance(requireContext())
        adapter.addAllCallLog(callLogUtils.readCallLogs())
        adapter.notifyDataSetChanged()
        onItemClickListener = object : CallLogAdapter.OnCallLogItemClickListener {
            override fun onItemClicked(callLogInfo: CallLogInfo) {
                val intent = Intent(context, ContactDetailsActivity::class.java).apply {
                    putExtra("CONTACT_NAME", callLogInfo.name)
                    putExtra("CONTACT_PHONE", callLogInfo.number)
                }
                startActivity(intent)
            }
        }
        adapter.setOnItemClickListener(onItemClickListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_READ_CALL_LOG -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setupRecyclerView()
                    loadData()
                } else {
                    Toast.makeText(context, "Permission denied to read call logs", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}
