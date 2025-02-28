package com.example.myapplication.emulator.fingerprintjs

import android.hardware.Sensor
import android.hardware.SensorManager

data class SensorData(
    val sensorName: String,
    val vendorName: String
)

interface SensorDataSource {
    fun sensors(): List<SensorData>
}

internal class SensorDataSourceImpl(
    private val sensorManager: SensorManager?
) : SensorDataSource {
    override fun sensors(): List<SensorData> {
        return sensorManager?.getSensorList(Sensor.TYPE_ALL)
            ?.map { sensor ->
                SensorData(
                    sensor.name ?: "Unknown",
                    sensor.vendor ?: "Unknown"
                )
            } ?: emptyList()
    }
}