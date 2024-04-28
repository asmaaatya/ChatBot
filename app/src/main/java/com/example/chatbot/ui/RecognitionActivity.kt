package com.example.chatbot.ui

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.chatbot.databinding.ActivityRecognitionBinding
import java.util.Locale

class RecognitionActivity : AppCompatActivity() {
    var binding: ActivityRecognitionBinding? = null
    var textToSpeech: TextToSpeech? = null
    private val RECOGNITION_RESULT = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecognitionBinding.inflate(layoutInflater)
        setContentView(binding?.getRoot())


        binding!!.botButton.setOnClickListener {
            val message = binding?.recognizedText?.text.toString()

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("message", message)
            startActivity(intent)
        }
        textToSpeech = TextToSpeech(this) { i ->
            if (i != TextToSpeech.ERROR) {
                textToSpeech!!.language = Locale.getDefault()
            }
        }
        binding!!.speakBtn.setOnClickListener(View.OnClickListener {
            val text: String = binding!!.recognizedText.getText().toString()
            textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        })
        binding!!.recognize.setOnClickListener(View.OnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            //                 intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
//                 intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "speech to text")
            startActivityForResult(intent, RECOGNITION_RESULT)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RECOGNITION_RESULT && resultCode == RESULT_OK) {
            val matches = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            binding!!.recognizedText.setText(matches!![0].toString())
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}