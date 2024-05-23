package com.example.callapp.Adapter

import android.app.Activity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.callapp.Data.CallLogData
import com.example.callapp.R
import java.text.DateFormat
import java.util.Date

class CallHistoryAdapter(private val callLogList: List<CallLogData>) : RecyclerView.Adapter<CallHistoryAdapter.CallLogViewHolder>() {
    private val CALL_PHONE_REQUEST_CODE = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.call_log_item, parent, false)
        return CallLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        val callLog = callLogList[position]
        if (callLog.name.isNullOrEmpty()){
            holder.nameTextView.text = callLog.number
        } else {
            holder.nameTextView.text = callLog.name
        }
        holder.dateTextView.text = DateFormat.getDateTimeInstance().format(Date(callLog.date))
        holder.typeTextView.text = when (callLog.type) {
            CallLog.Calls.INCOMING_TYPE -> "Incoming"
            CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
            CallLog.Calls.MISSED_TYPE -> "Missed"
            else -> "Unknown"
        }

        // Set click listener for the callPerson view
        holder.callPersonLayout.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:${callLog.number}")

            // Check if permission is granted before making the call
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                context.startActivity(intent)
            } else {
                // Request CALL_PHONE permission if not granted
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), CALL_PHONE_REQUEST_CODE)
            }
        }
    }


    override fun getItemCount(): Int = callLogList.size

    class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val callPersonLayout: LinearLayout = itemView.findViewById(R.id.callPerson)
        val nameTextView: TextView = itemView.findViewById(R.id.callLogName)
//        val numberTextView: TextView = itemView.findViewById(R.id.callLogNumber)
        val dateTextView: TextView = itemView.findViewById(R.id.callLogDate)
        val typeTextView: TextView = itemView.findViewById(R.id.callLogType)
    }
}
