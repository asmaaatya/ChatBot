package com.example.chatbot.utils

import com.example.chatbot.data.Message
import com.example.chatbot.utils.Constants.OPEN_GOOGLE
import com.example.chatbot.utils.Constants.OPEN_SEARCH

object BotResponse {
    fun basicresponse(myMessage: String): String {
        val random=(0..2).random()
        val mes=myMessage.toLowerCase()
       return when {
           mes.contains("hello") ->{
               when (random){
                   0 -> "hello there"
                   1 -> "welcome again"
                   2 -> "hello"
                   else -> "error"
               }
           }
           mes.contains("how are you") ->{
               when (random){
                   0 -> "fine"
                   1 -> "all right"
                   2 -> "good"
                   else -> "error"
               }
           }
           mes.contains("open")&&mes.contains("google") ->{OPEN_GOOGLE}
           mes.contains("search") ->{
               OPEN_SEARCH}
           mes.contains("time")&&mes.contains("?")->{Time.timeStamp()}
           else -> {
               when (random){
                   0 -> "I am not understand"
                   1 -> "Idk"
                   2 -> "try to explain"
                   else -> "error"
               }
           }
       }
    }

}