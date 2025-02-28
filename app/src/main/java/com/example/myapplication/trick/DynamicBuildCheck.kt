package com.example.myapplication.trick

class DynamicBuildCheck {
    fun dynamicBuildCheck(): Boolean {
        return try {
            val clazz = Class.forName("com.example.myapplication.emulator.BuildEmDetect")
            val constructor = clazz.getConstructor()
            val instance = constructor.newInstance()
            val method = clazz.getDeclaredMethod("isEmulator")
            method.isAccessible = true
            method.invoke(instance) as Boolean
        } catch (e: Exception) {
            // If reflection fails for any reason, you might default to a safe value.
            true
        }
    }

    fun dynamicBuildString(): String {
        return try {
            val clazz = Class.forName("com.example.myapplication.emulator.BuildEmDetect")
            val constructor = clazz.getConstructor()
            val instance = constructor.newInstance()
            val method = clazz.getDeclaredMethod("getBuildInfo")
            method.isAccessible = true
            method.invoke(instance) as String
        } catch (e: Exception) {
            // If reflection fails for any reason, you might default to a safe value.
            "Something Broke?"
        }
    }
}