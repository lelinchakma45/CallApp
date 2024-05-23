package com.example.callapp.Data

data class CallLogInfo(
    var name: String? = null,
    var number: String? = null,
    var callType: String? = null,
    var date: Long = 0,
    var duration: Long = 0
)
