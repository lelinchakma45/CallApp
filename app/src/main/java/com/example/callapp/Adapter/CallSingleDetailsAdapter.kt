package com.example.callapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.callapp.Data.CallDetailsSingleData
import com.example.callapp.R
import com.example.callapp.Utils.Utils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class CallSingleDetailsAdapter(private val callLogs: List<CallDetailsSingleData>) : RecyclerView.Adapter<CallSingleDetailsAdapter.CallLogViewHolder>() {

    class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewType: TextView = itemView.findViewById(R.id.textViewType)
//        val textViewCount: TextView = itemView.findViewById(R.id.textViewCount)
        val textViewTime: TextView = itemView.findViewById(R.id.timeShow) // Add TextView for time
        val textViewDuration: TextView = itemView.findViewById(R.id.textViewDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_call_log, parent, false)
        return CallLogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        val callLog = callLogs[position]
        holder.textViewType.text = callLog.type
//        holder.textViewCount.text = "${callLog.count} calls"

        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - callLog.timestamp

        val timeAgo = when {
            timeDifference < TimeUnit.MINUTES.toMillis(1) -> "a moment ago"
            timeDifference < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(timeDifference)} minutes ago"
            timeDifference < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(timeDifference)} hours ago"
            isToday(callLog.timestamp) -> "today"
            isYesterday(callLog.timestamp) -> "yesterday"
            else -> SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(callLog.timestamp))
        }

        holder.textViewTime.text = timeAgo
        holder.textViewDuration.text = Utils.formatSeconds(callLog.duration)
    }
    private fun isToday(timestamp: Long): Boolean {
        val todayCalendar = Calendar.getInstance()
        val logCalendar = Calendar.getInstance()
        logCalendar.timeInMillis = timestamp
        return todayCalendar.get(Calendar.YEAR) == logCalendar.get(Calendar.YEAR) &&
                todayCalendar.get(Calendar.DAY_OF_YEAR) == logCalendar.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(timestamp: Long): Boolean {
        val yesterdayCalendar = Calendar.getInstance()
        yesterdayCalendar.add(Calendar.DAY_OF_YEAR, -1)
        val logCalendar = Calendar.getInstance()
        logCalendar.timeInMillis = timestamp
        return yesterdayCalendar.get(Calendar.YEAR) == logCalendar.get(Calendar.YEAR) &&
                yesterdayCalendar.get(Calendar.DAY_OF_YEAR) == logCalendar.get(Calendar.DAY_OF_YEAR)
    }

    override fun getItemCount() = callLogs.size
}