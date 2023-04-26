package com.example.chatbot.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbot.R
import com.example.chatbot.data.Message
import com.example.chatbot.databinding.MessageItemBinding
import com.example.chatbot.utils.Constants.RECEIVE_ID
import com.example.chatbot.utils.Constants.SEND_ID

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageHolder>() {
    //message list
    var messageList= mutableListOf<Message>()
   inner class MessageHolder(view: View) : RecyclerView.ViewHolder(view)  {
       val binding = MessageItemBinding.bind(view)
     init {
         view.setOnClickListener {
             messageList.removeAt(adapterPosition)
             notifyItemRemoved(adapterPosition)
         }
     }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        return MessageHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_item,null,false))
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        with(holder) {
            val currentMessage = messageList[position]
            when (currentMessage.id) {
                SEND_ID -> {binding.rightChatText.apply {
                    text=currentMessage.message
                    visibility=View.VISIBLE
                }
                    binding.leftChatText.visibility=View.GONE
                }

                RECEIVE_ID-> {binding.leftChatText.apply {
                    text=currentMessage.message
                    visibility=View.VISIBLE
                }
                    binding.rightChatText.visibility=View.GONE}
            }
        }
    }

    override fun getItemCount(): Int {
      return messageList.size
    }
fun insertMessage(message: Message){
    this.messageList.add(message)
    notifyItemInserted(messageList.size)
}
}