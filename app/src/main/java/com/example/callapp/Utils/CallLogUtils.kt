package com.example.callapp.Utils

import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import android.util.Log
import com.example.callapp.Data.CallLogInfo

class CallLogUtils private constructor(private val context: Context) {

    private val mainList = ArrayList<CallLogInfo>()
    private val missedCallList = ArrayList<CallLogInfo>()
    private val outgoingCallList = ArrayList<CallLogInfo>()
    private val incomingCallList = ArrayList<CallLogInfo>()

    companion object {
        @Volatile
        private var instance: CallLogUtils? = null

        fun getInstance(context: Context): CallLogUtils =
            instance ?: synchronized(this) {
                instance ?: CallLogUtils(context).also { instance = it }
            }
    }

    private fun loadData() {
        val projection = arrayOf("_id", CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.TYPE, CallLog.Calls.CACHED_NAME)
        val contentResolver = context.applicationContext.contentResolver
        val cursor: Cursor? = contentResolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DEFAULT_SORT_ORDER)

        cursor?.use {
            if (cursor.count == 0) {
                Log.d("CALLLOG", "cursor size is 0")
                return
            }

            while (cursor.moveToNext()) {
                val callLogInfo = CallLogInfo().apply {
                    name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                    number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                    callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))
                    date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
                    duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION))
                }
                mainList.add(callLogInfo)

                when (callLogInfo.callType?.toInt()) {
                    CallLog.Calls.OUTGOING_TYPE -> outgoingCallList.add(callLogInfo)
                    CallLog.Calls.INCOMING_TYPE -> incomingCallList.add(callLogInfo)
                    CallLog.Calls.MISSED_TYPE -> missedCallList.add(callLogInfo)
                }
            }
        }
    }

    fun readCallLogs(): ArrayList<CallLogInfo> {
        if (mainList.isEmpty()) loadData()
        return mainList
    }

    fun getMissedCalls(): ArrayList<CallLogInfo> {
        if (mainList.isEmpty()) loadData()
        return missedCallList
    }

    fun getIncomingCalls(): ArrayList<CallLogInfo> {
        if (mainList.isEmpty()) loadData()
        return incomingCallList
    }

    fun getOutgoingCalls(): ArrayList<CallLogInfo> {
        if (mainList.isEmpty()) loadData()
        return outgoingCallList
    }

    fun getAllCallState(number: String): LongArray {
        var callCount = 0L
        var callDuration = 0L

        for (callLogInfo in mainList) {
            if (callLogInfo.number == number) {
                callCount++
                if (callLogInfo.callType?.toInt() != CallLog.Calls.MISSED_TYPE) {
                    callDuration += callLogInfo.duration
                }
            }
        }
        return longArrayOf(callCount, callDuration)
    }

    fun getIncomingCallState(number: String): LongArray {
        var callCount = 0L
        var callDuration = 0L

        for (callLogInfo in incomingCallList) {
            if (callLogInfo.number == number) {
                callCount++
                callDuration += callLogInfo.duration
            }
        }
        return longArrayOf(callCount, callDuration)
    }

    fun getOutgoingCallState(number: String): LongArray {
        var callCount = 0L
        var callDuration = 0L

        for (callLogInfo in outgoingCallList) {
            if (callLogInfo.number == number) {
                callCount++
                callDuration += callLogInfo.duration
            }
        }
        return longArrayOf(callCount, callDuration)
    }

    fun getMissedCallState(number: String): Int {
        var missedCallCount = 0

        for (callLogInfo in missedCallList) {
            if (callLogInfo.number == number) {
                missedCallCount++
            }
        }
        return missedCallCount
    }
}