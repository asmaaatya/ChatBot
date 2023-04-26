package com.example.chatbot.utils

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object Time {
    fun timeStamp() :String{
        val timestamp=Timestamp(System.currentTimeMillis())
        val sdf=SimpleDateFormat("HH:mm")
        val time=sdf.format(Date(timestamp.time))
        return time.toString()
    }
}