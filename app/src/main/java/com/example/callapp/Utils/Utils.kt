package com.example.callapp.Utils

object Utils {
    fun formatSeconds(timeInSeconds: Long): String {
        val hours = (timeInSeconds / 3600).toInt()
        val secondsLeft = (timeInSeconds - hours * 3600).toInt()
        val minutes = secondsLeft / 60
        val seconds = secondsLeft % 60

        var formattedTime = ""

        if (hours > 0) {
            formattedTime += "${hours}h "
        }

        formattedTime += "${minutes}m ${seconds}s"

        return formattedTime
    }
}
