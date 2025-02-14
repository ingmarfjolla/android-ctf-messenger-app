package com.example.myapplication.emulator
import android.os.Build

class BuildEmDetect {

    fun isEmulator(): Boolean
    {
        return (Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.startsWith("unknown") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86") ||
                Build.MODEL.contains("x86") ||
                Build.MANUFACTURER.contains("Genymotion") ||
                Build.BRAND.startsWith("generic") ||
                Build.DEVICE.startsWith("generic") ||
                Build.PRODUCT == "google_sdk" ||
                Build.BOARD.startsWith("goldfish"))
    }

    fun getBuildInfo(): String {
        return """
            FINGERPRINT: ${Build.FINGERPRINT}
            MODEL: ${Build.MODEL}
            MANUFACTURER: ${Build.MANUFACTURER}
            BRAND: ${Build.BRAND}
            DEVICE: ${Build.DEVICE}
            BOARD: ${Build.BOARD}
            HOST: ${Build.HOST}
            PRODUCT: ${Build.PRODUCT}
        """.trimIndent()
    }

}