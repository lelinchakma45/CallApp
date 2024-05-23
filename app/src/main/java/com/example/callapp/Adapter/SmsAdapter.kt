package com.example.callapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.callapp.Data.SmsData
import com.example.callapp.R

class SmsAdapter(
    private val smsList: List<SmsData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SmsAdapter.SmsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sms_item, parent, false)
        return SmsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        val sms = smsList[position]
        holder.tvAddress.text = sms.address
        holder.tvBody.text = sms.body
        holder.itemView.setOnClickListener {
            listener.onItemClick(sms)
        }
    }

    override fun getItemCount(): Int = smsList.size

    class SmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvBody: TextView = itemView.findViewById(R.id.tvBody)
    }

    interface OnItemClickListener {
        fun onItemClick(sms: SmsData)
    }
}