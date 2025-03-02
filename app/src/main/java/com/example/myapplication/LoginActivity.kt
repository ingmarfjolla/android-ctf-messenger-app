package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.emulator.BuildEmDetect
import com.example.myapplication.emulator.ThreadDetectNew
import com.example.myapplication.emulator.fingerprintjs.CpuInfoProviderImpl
import com.example.myapplication.emulator.fingerprintjs.SensorDataSourceImpl
import com.google.android.material.snackbar.Snackbar
import com.example.myapplication.password.PasswordHandler
import com.example.myapplication.trick.DynamicBuildCheck


class LoginActivity : AppCompatActivity() {


    private val correctPassword = "secret123"

    init {
        System.loadLibrary("myapplication")
    }
    private external fun getNativeKeyPart(): String
    private external fun getNativePassword(): String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        ///testing em check ////
        val detector = DynamicBuildCheck()
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
        val threadInfo = ThreadDetectNew()
        val threadInfoStr = threadInfo.getThreadInfo()
        val fdSuspicious = threadInfoStr.contains("goldfish", ignoreCase = true)
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
        val logcheck = sensorSuspicious || detector.dynamicBuildCheck() || fdSuspicious || cpuSuspicious
        Log.i("Login Emulator is:", logcheck.toString())
        ///////////////////////////////////


        // references to the UI components
        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener { view ->
            // stuff for UI inputs //
            val username = usernameEditText.text.toString() // if you need it later
            val password = passwordEditText.text.toString()
            ////////////////////////

            // stuff for password encryption //
            val nativeKeyPart = getNativeKeyPart()
            val nativePassword = getNativePassword()
            val encryptionHandler = PasswordHandler()
            val aespasswd = encryptionHandler.generateSecretKey(correctPassword+nativeKeyPart)
            val encryptedInputPass = encryptionHandler.encrypt(password,aespasswd)
            val encryptedUserName = encryptionHandler.encrypt(username,aespasswd)
            ////////////////////////

            if (encryptedInputPass == nativePassword) {
                // can't test toast for some reason, changing to snackbar.
                //Toast.makeText(this, "Login successful! Welcome back comrade", Toast.LENGTH_SHORT).show()
                Snackbar.make(view,"Login successful! Welcome back comrade", Snackbar.LENGTH_SHORT).show()
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
                finish()  // Optionally finish the login activity so the user can’t go back to it
            }
            else if(encryptedUserName == nativePassword){
                Snackbar.make(view, "blyat check the field!", Snackbar.LENGTH_SHORT).show()
                //Toast.makeText(this, "blyat check the field!", Toast.LENGTH_SHORT).show()
            }
            else {
                Snackbar.make(view, "Invalid password", Snackbar.LENGTH_SHORT).show()
                //Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
