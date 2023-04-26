package com.example.chatbot.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatbot.R
import com.example.chatbot.databinding.ActivityMainBinding
import com.example.chatbot.utils.BotResponse
import com.example.chatbot.utils.Constants
import com.example.chatbot.utils.Constants.OPEN_GOOGLE
import com.example.chatbot.utils.Constants.OPEN_SEARCH
import com.example.chatbot.utils.Constants.RECEIVE_ID
import com.example.chatbot.utils.Constants.SEND_ID
import com.example.chatbot.utils.Time
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    private lateinit var adapter: MessageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        recyclerview()
        clickEvents()
        customMessage("Hello how can i help you?")

    }

    private fun clickEvents() {
binding.sendIcon.setOnClickListener { sendMessage() }
        binding.messageContent.setOnClickListener {  GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main){
                binding.messageList.scrollToPosition(adapter.itemCount-1)
            }
        } }
    }

    private fun recyclerview() {
       adapter= MessageAdapter()
        binding.messageList.adapter=adapter
        binding.messageList.layoutManager=LinearLayoutManager(applicationContext)
    }
    private fun sendMessage(){
        val msg=binding.messageContent.text.toString()
        val timestamp=Time.timeStamp()
        if(msg.isNotEmpty()){
            binding.messageContent.setText("")
            adapter.insertMessage(com.example.chatbot.data.Message(msg, SEND_ID,timestamp))
            binding.messageList.scrollToPosition(adapter.itemCount-1)
            botResponse(msg)
        }
    }

    private fun botResponse(msg: String) {
        val timestamp=Time.timeStamp()
    GlobalScope.launch {
        delay(1000L)
        withContext(Dispatchers.Main){
            val resp=BotResponse.basicresponse(msg)
            adapter.insertMessage(com.example.chatbot.data.Message(resp, RECEIVE_ID,timestamp))
            binding.messageList.scrollToPosition(adapter.itemCount-1)
            when(resp){
                OPEN_GOOGLE->{
                val website=Intent(Intent.ACTION_VIEW)
                    website.data= Uri.parse("https://www.google.com/")
                    startActivity(website)
                }
                OPEN_SEARCH ->{
                    val website=Intent(Intent.ACTION_VIEW)
                    val search:String?=msg.substringAfter("search")
                    website.data= Uri.parse("https://www.google.com/search?&q=$search")
                    startActivity(website)
                }
            }
        }
    }

    }

    private fun customMessage(message: String){
        GlobalScope.launch {
            delay(1000L)
            withContext(Dispatchers.Main){
                val timestamp=Time.timeStamp()
                adapter.insertMessage(com.example.chatbot.data.Message(message, RECEIVE_ID,timestamp))
                binding.messageList.scrollToPosition(adapter.itemCount-1)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                binding.messageList.scrollToPosition(adapter.itemCount-1)
            }
        }
    }
}