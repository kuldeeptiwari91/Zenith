package com.kuldeep.zenith

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userPrompt = findViewById<TextView>(R.id.userPrompt)
        val botResponse = findViewById<TextView>(R.id.botResponse)
        val btnSend = findViewById<ImageButton>(R.id.btnSend)
        val userInput = findViewById<EditText>(R.id.userInput)

        btnSend.setOnClickListener {
            val prompt = userInput.text.toString()
            userPrompt.text = prompt
            val generativeModel = GenerativeModel(
                modelName = "gemini-pro",
                apiKey = resources.getString(R.string.gemini_api_key)
            )
            userInput.text.clear()

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = generativeModel.generateContent(prompt)
                    launch(Dispatchers.Main) {
                        botResponse.text = response.text
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
