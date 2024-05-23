package com.example.callapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.callapp.Data.SmsDetailsData
import com.example.callapp.R

class SmsDetailsAdapter(private val smsList: MutableList<SmsDetailsData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val INCOMING_MESSAGE = 1
    private val OUTGOING_MESSAGE = 2

    override fun getItemViewType(position: Int): Int {
        return if (smsList[position].isIncoming) INCOMING_MESSAGE else OUTGOING_MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == INCOMING_MESSAGE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_incoming_message, parent, false)
            IncomingMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_outgoing_message, parent, false)
            OutgoingMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sms = smsList[position]
        if (holder is IncomingMessageViewHolder) {
            holder.bind(sms)
        } else if (holder is OutgoingMessageViewHolder) {
            holder.bind(sms)
        }
    }

    fun addMessage(sms: SmsDetailsData) {
        smsList.add(sms)
        notifyItemInserted(smsList.size - 1)
    }
    override fun getItemCount(): Int = smsList.size

    class IncomingMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        fun bind(sms: SmsDetailsData) {
            tvMessage.text = sms.body
        }
    }

    class OutgoingMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        fun bind(sms: SmsDetailsData) {
            tvMessage.text = sms.body
        }
    }
}
