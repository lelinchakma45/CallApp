package com.example.callapp.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
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

    @Nullable
    override fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = FragmentRecentBinding.inflate(layoutInflater)
        initComponents()
        return binding.root
    }

    private fun initComponents() {
        val mLayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = mLayoutManager
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = CallLogAdapter(requireContext())
        binding.recyclerView.adapter = adapter
        loadData()
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
}