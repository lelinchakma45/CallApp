package com.example.callapp.Utils
import android.content.Context
import com.example.callapp.Data.CallDetailsSingleData

class LogCallUtils private constructor(context: Context) {
    companion object {
        private var instance: LogCallUtils? = null

        fun getInstance(context: Context): LogCallUtils {
            if (instance == null) {
                instance = LogCallUtils(context)
            }
            return instance!!
        }
    }

    fun getAllCallLogs(number: String): List<CallDetailsSingleData> {
        val callLogs = mutableListOf<CallDetailsSingleData>()

        // Dummy call logs
        val incomingCalls = listOf(
            CallDetailsSingleData("Incoming", System.currentTimeMillis() - 60000, 150L),
            CallDetailsSingleData("Incoming", System.currentTimeMillis() - 120000, 180L)
        )

        val outgoingCalls = listOf(
            CallDetailsSingleData("Outgoing", System.currentTimeMillis() - 180000, 100L)
        )

        val missedCalls = listOf(
            CallDetailsSingleData("Missed", System.currentTimeMillis() - 240000, 0L),
            CallDetailsSingleData("Missed", System.currentTimeMillis() - 300000, 0L)
        )

        callLogs.addAll(incomingCalls)
        callLogs.addAll(outgoingCalls)
        callLogs.addAll(missedCalls)

        callLogs.sortByDescending { it.timestamp }

        return callLogs
    }
}