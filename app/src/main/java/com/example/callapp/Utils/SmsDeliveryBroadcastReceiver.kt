package com.example.callapp.Utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.widget.Toast

class SmsDeliveryBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.extras
        val phoneNumber = bundle?.getString("phoneNumber")
        val resultCode = resultCode

        when (resultCode) {
            SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                Toast.makeText(
                    context,
                    "SMS delivery failed for $phoneNumber",
                    Toast.LENGTH_SHORT
                ).show()
            }
            SmsManager.RESULT_ERROR_NO_SERVICE -> {
                Toast.makeText(
                    context,
                    "No service available for sending SMS to $phoneNumber",
                    Toast.LENGTH_SHORT
                ).show()
            }
            SmsManager.RESULT_ERROR_NULL_PDU -> {
                Toast.makeText(
                    context,
                    "Null PDU for sending SMS to $phoneNumber",
                    Toast.LENGTH_SHORT
                ).show()
            }
            SmsManager.RESULT_ERROR_RADIO_OFF -> {
                Toast.makeText(
                    context,
                    "Radio off while sending SMS to $phoneNumber",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                Toast.makeText(
                    context,
                    "SMS delivered successfully to $phoneNumber",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
