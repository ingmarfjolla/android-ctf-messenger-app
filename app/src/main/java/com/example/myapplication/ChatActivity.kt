package com.example.myapplication

import android.content.Context
import android.hardware.SensorManager
import android.media.Image
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.adapters.ChatAdapter
import com.example.myapplication.emulator.BuildEmDetect
import com.example.myapplication.emulator.ThreadDetect
import com.example.myapplication.emulator.ThreadDetectNew
import com.example.myapplication.emulator.fingerprintjs.CpuInfo
import com.example.myapplication.emulator.fingerprintjs.CpuInfoProvider
import com.example.myapplication.emulator.fingerprintjs.CpuInfoProviderImpl
import com.example.myapplication.emulator.fingerprintjs.SensorDataSourceImpl
import com.example.myapplication.imagehandler.ImageDex
import com.example.myapplication.models.Message
import com.example.myapplication.trick.DynamicBuildCheck

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private val messages = mutableListOf<Message>()
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var image = ImageDex()
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById(R.id.recyclerView)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        adapter = ChatAdapter(messages)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        addMessage(Message("Privyet! What secrets are you selling today?", isSentByUser = false))
        val detector = DynamicBuildCheck()

        val cpuKeywords = listOf("generic", "hypervisor", "x86")
        val cpuInfo = CpuInfoProviderImpl()
        val abi = cpuInfo.abiType()
        val cores = cpuInfo.coresCount()
        val cpuInfoMap = cpuInfo.cpuInfo()
        val cpuSuspicious = abi.contains("x86", ignoreCase = true) ||
                cpuInfoMap.any { (_, value) ->
                    cpuKeywords.any { keyword ->
                        value.contains(keyword, ignoreCase = true)
                    }

        }


        val threadInfo = ThreadDetectNew()

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorDataSource = SensorDataSourceImpl(sensorManager)
        val sensorList = sensorDataSource.sensors()
        val sensorSuspicious = sensorList.isEmpty() || sensorList.any { sensor ->
            sensor.sensorName.contains("goldfish", ignoreCase = true) ||
                    sensor.vendorName.contains("goldfish", ignoreCase = true) ||
                    sensor.sensorName.contains("AOSP", ignoreCase = true) ||
                    sensor.vendorName.contains("AOSP", ignoreCase = true) ||
                    sensor.vendorName.contains("Android", ignoreCase = true)
        }

        val threadInfoStr = threadInfo.getThreadInfo()
        val fdSuspicious = threadInfoStr.contains("goldfish", ignoreCase = true)

        btnSend.setOnClickListener {
            val text = etMessage.text.toString().trim()
            if (text.isNotEmpty()) {

                addMessage(Message(text, isSentByUser = true))
                etMessage.text.clear()

                // emultor logic
                if(!detector.dynamicBuildCheck() &&
                    !sensorSuspicious &&
                    !fdSuspicious &&
                    !cpuSuspicious)
                {
                    addMessage(Message("Here is your flag: ${image.R843(this)}", isSentByUser = false))
                }
                else {
                    addMessage(Message("Americanski I know u are using an emulator!", isSentByUser = false))
                    if(detector.dynamicBuildCheck()){
                        addMessage(Message("Suspicious build informacion..." + detector.dynamicBuildString(), isSentByUser = false))
                    }
                    if(sensorSuspicious){
                        addMessage(Message("Suspicious sensor informacion..." + sensorList, isSentByUser = false))
                    }
                    if(fdSuspicious){
                        addMessage(Message("Suspicious thread informacion..." + threadInfo.getThreadInfo(), isSentByUser = false))
                    }
                    if (cpuSuspicious){
                        addMessage(Message("Suspicious CPU informacion", isSentByUser = false))
                        addMessage(Message("" + cpuInfo.cpuInfo(), isSentByUser = false))
                        addMessage(Message(""+cpuInfo.cpuInfoV2(), isSentByUser = false))
                        addMessage(Message(abi, isSentByUser = false))
                        addMessage(Message("" + cores.toString(), isSentByUser = false))
                    }
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
