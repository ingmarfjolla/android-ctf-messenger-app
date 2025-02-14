package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.adapters.ChatAdapter
import com.example.myapplication.emulator.BuildEmDetect
import com.example.myapplication.models.Message

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private val messages = mutableListOf<Message>()
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById(R.id.recyclerView)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        adapter = ChatAdapter(messages)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        addMessage(Message("Privyet! What secrets are you selling today?", isSentByUser = false))
        val detector = BuildEmDetect()
        btnSend.setOnClickListener {
            val text = etMessage.text.toString().trim()
            if (text.isNotEmpty()) {

                addMessage(Message(text, isSentByUser = true))
                etMessage.text.clear()

                // emultor logic
                if(!detector.isEmulator()){
                    addMessage(Message("Here is your flag: winner", isSentByUser = false))
                }
                else {
                    addMessage(Message("Americanski I know u are using an emulator!" + detector.getBuildInfo(), isSentByUser = false))
                }
            }
        }
    }

    private fun addMessage(message: Message) {
        messages.add(message)
        adapter.notifyItemInserted(messages.size - 1)
        recyclerView.scrollToPosition(messages.size - 1)
    }
}
