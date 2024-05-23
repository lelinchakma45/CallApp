package com.example.callapp.Adapter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.CallLog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.callapp.Data.CallLogInfo
import com.example.callapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CallLogAdapter(private val context: Context) : RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {
    private val CALL_PHONE_REQUEST_CODE = 1
    private val callLogInfoArrayList = ArrayList<CallLogInfo>()
    private val callLogCountMap = HashMap<String, Int>()
    private var onItemClickListener: OnCallLogItemClickListener? = null

    interface OnCallLogItemClickListener {
        fun onItemClicked(callLogInfo: CallLogInfo)
    }

    fun setOnItemClickListener(listener: OnCallLogItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return CallLogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        holder.bind(callLogInfoArrayList[position])
    }

    fun addCallLog(callLogInfo: CallLogInfo) {
        if (callLogInfo.number != null && !callLogCountMap.containsKey(callLogInfo.number)) {
            callLogInfoArrayList.add(callLogInfo)
            callLogCountMap[callLogInfo.number!!] = 1
        } else if (callLogInfo.number != null) {
            callLogCountMap[callLogInfo.number!!] = callLogCountMap.getOrDefault(callLogInfo.number!!, 0) + 1
        }
        notifyDataSetChanged()
    }

    fun addAllCallLog(list: ArrayList<CallLogInfo>) {
        val uniqueNumbers = HashSet<String>()
        callLogInfoArrayList.clear()
        callLogCountMap.clear()

        for (callLogInfo in list) {
            callLogInfo.number?.let { number ->
                if (uniqueNumbers.add(number)) {
                    callLogInfoArrayList.add(callLogInfo)
                    callLogCountMap[number] = 1
                } else {
                    callLogCountMap[number] = callLogCountMap.getOrDefault(number, 0) + 1
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return callLogInfoArrayList.size
    }

    inner class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeCall: ImageView = itemView.findViewById(R.id.typeCall)
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val textViewCallDate: TextView = itemView.findViewById(R.id.textViewCallDate)
        private val nameDetails: LinearLayout = itemView.findViewById(R.id.nameDetails)
        private val callBtn: LinearLayout = itemView.findViewById(R.id.call_btn)

        fun bind(callLog: CallLogInfo) {
            // Set call type icon and color based on call type
            when (callLog.callType?.toInt()) {
                CallLog.Calls.OUTGOING_TYPE -> {
                    typeCall.setImageResource(R.drawable.call_outgoing)
                    typeCall.setColorFilter(ContextCompat.getColor(context, R.color.green))
                }
                CallLog.Calls.INCOMING_TYPE -> {
                    typeCall.setImageResource(R.drawable.call_incoming)
                    typeCall.setColorFilter(ContextCompat.getColor(context, R.color.blue))
                }
                CallLog.Calls.MISSED_TYPE -> {
                    typeCall.setImageResource(R.drawable.call_missed)
                    typeCall.setColorFilter(ContextCompat.getColor(context, R.color.red))
                }
            }

            // Display contact name or number
            val number = callLog.number
            val name = if (TextUtils.isEmpty(callLog.name)) number else callLog.name
            val callCount = callLogCountMap[number] ?: 1
            textViewName.text = if (callCount > 1) "$name ($callCount)" else name

            // Format and display call date
            val dateObj = Date(callLog.date)
            val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            textViewCallDate.text = formatter.format(dateObj)

            // Set click listeners
            nameDetails.setOnClickListener {
                onItemClickListener?.onItemClicked(callLog)
            }
            callBtn.setOnClickListener {
                callRequest(number)
            }
        }

        private fun callRequest(contactPhone: String?) {
            contactPhone?.let { phone ->
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$phone")
                // Check if the CALL_PHONE permission is granted
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(intent)
                } else {
                    // Request CALL_PHONE permission
                    ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), CALL_PHONE_REQUEST_CODE)
                }
            }
        }
    }
}
