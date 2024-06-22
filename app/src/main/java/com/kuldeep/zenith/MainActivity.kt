package com.kuldeep.zenith

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<Message>()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val userInput = findViewById<EditText>(R.id.userInput)
        val btnSend = findViewById<ImageButton>(R.id.btnSend)

        messageAdapter = MessageAdapter(messageList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter

        btnSend.setOnClickListener {
            val prompt = userInput.text.toString()
            if (prompt.isNotEmpty()) {
                val generativeModel = GenerativeModel(
                    modelName = "gemini-pro",
                    apiKey = resources.getString(R.string.gemini_api_key)
                )
                userInput.text.clear()

                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response = generativeModel.generateContent(prompt)
                        launch(Dispatchers.Main) {
                            val message = response.text?.let { it1 -> Message(prompt, it1) }
                            if (message != null) {
                                messageList.add(message)
                            }
                            messageAdapter.notifyItemInserted(messageList.size - 1)
                            recyclerView.scrollToPosition(messageList.size - 1)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
