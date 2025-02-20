package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import android.opengl.GLES20
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.example.myapplication.emulator.BuildEmDetect
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MainActivity : AppCompatActivity() {
    private val splashDelay: Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val splashLogo = findViewById<ImageView>(R.id.splashLogo)
        val detector = BuildEmDetect()
        val check = detector.isEmulator()
        Log.d("Emulator is:", check.toString())
        ObjectAnimator.ofFloat(splashLogo, "rotation", 0f, 360f).apply {
            duration = 4000  // 1 second per rotation
            repeatCount = ObjectAnimator.INFINITE
            start()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, splashDelay)


    }
}
