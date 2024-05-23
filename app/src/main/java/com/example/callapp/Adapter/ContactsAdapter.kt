package com.example.callapp.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.callapp.ContactDetailsActivity
import com.example.callapp.Data.ContactData
import com.example.callapp.R
import java.util.Locale

class ContactsAdapter(private val contactsList: List<ContactData>) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(view)
    }

    fun String.firstLetterUpperCase(): String {
        return if (this.isNotEmpty()) this[0].uppercase(Locale.getDefault()) else ""
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactsList[position]
        holder.nameTextView.text = contact.name
        holder.phoneTextView.text = contact.phoneNumber
        holder.profileLetter.text = contact.name.firstLetterUpperCase()

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ContactDetailsActivity::class.java).apply {
                putExtra("CONTACT_NAME", contact.name)
                putExtra("CONTACT_PHONE", contact.phoneNumber)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = contactsList.size

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.contactName)
        val phoneTextView: TextView = itemView.findViewById(R.id.contactPhoneNumber)
        val profileLetter: TextView = itemView.findViewById(R.id.profileLetter)
    }
}

