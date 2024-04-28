package com.example.chatbot.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.TextView
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
import java.util.Locale

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MessageAdapter
    var textToSpeech: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        textToSpeech = TextToSpeech(this) { i ->
            if (i != TextToSpeech.ERROR) {
                textToSpeech!!.language = Locale.getDefault()
            }
        }
        recyclerview()


        // Check if the intent contains the "message" extra
        if (intent.hasExtra("message")) {
            // Retrieve the message extra
            val message = intent.getStringExtra("message")
            binding.messageContent.setText(message)
        }
        clickEvents()


    }

    private fun clickEvents() {
        binding.sendIcon.setOnClickListener { sendMessage() }
        binding.speakIcon.setOnClickListener {
            val viewsWithText = listOf<View>(binding.welcomeText, binding.messageContent)
            for (view in viewsWithText) {
                if (view is TextView) {
                    val text = view.text.toString()
                    if (text.isNotBlank()) {
                        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                        // Add delay between each text if needed
                        Thread.sleep(2000)
                    }
                }
            }
            speakOutRecyclerViewData()
        }
        binding.messageContent.setOnClickListener {
            GlobalScope.launch {
                delay(100)
                withContext(Dispatchers.Main) {
                    binding.messageList.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private fun speakOutRecyclerViewData() {
        val dataList =
            adapter.getDataList() // Replace getDataList() with a method in your adapter that returns the list of data

        for (data in dataList) {
            textToSpeech!!.speak(data, TextToSpeech.QUEUE_ADD, null, null)
        }
    }

    private fun recyclerview() {
        adapter = MessageAdapter()
        binding.messageList.adapter = adapter
        binding.messageList.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun sendMessage() {
        // Retrieve the intent
        val intent = intent

        // Check if the intent contains the "message" extra
        if (intent.hasExtra("message")) {
            // Retrieve the message extra
            val message = intent.getStringExtra("message")

            val timestamp = Time.timeStamp()
            if (message!!.isNotEmpty()) {
                binding.messageContent.setText("")
                adapter.insertMessage(com.example.chatbot.data.Message(message, SEND_ID, timestamp))
                binding.messageList.scrollToPosition(adapter.itemCount - 1)

                botResponse(message)
            }
        }
    }

    private fun botResponse(msg: String) {
        val timestamp = Time.timeStamp()
        GlobalScope.launch {
            delay(1000L)
            withContext(Dispatchers.Main) {
                val resp = BotResponse.basicResponse(msg)
                adapter.insertMessage(com.example.chatbot.data.Message(resp, RECEIVE_ID, timestamp))
                binding.messageList.scrollToPosition(adapter.itemCount - 1)
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

    private fun customMessage(message: String) {
        GlobalScope.launch {
            delay(1000L)
            withContext(Dispatchers.Main) {
                val timestamp = Time.timeStamp()
                adapter.insertMessage(
                    com.example.chatbot.data.Message(
                        message,
                        RECEIVE_ID,
                        timestamp
                    )
                )
                binding.messageList.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                binding.messageList.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}