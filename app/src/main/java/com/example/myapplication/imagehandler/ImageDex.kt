package com.example.myapplication.imagehandler;

import android.content.Context
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


public class ImageDex {
    fun R843(context: Context) : String {
        var item = ""
        try {
            val assetManager = context.assets
            val dexFileName = "payload.dex"

            // Step 1: Copy the dex file from assets to a readable but non-writable location
            val dexDir = File(context.codeCacheDir, "dex") // Use a dedicated dex folder
            if (!dexDir.exists()) dexDir.mkdirs() // Ensure directory exists

            val dexFile = File(dexDir, dexFileName)
            if (!dexFile.exists()) {
                assetManager.open(dexFileName).use { inputStream ->
                        FileOutputStream(dexFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                }
                }
            }
            dexFile.setReadOnly()


            // Step 2: Create an optimized directory for DexClassLoader
            //val optimizedDir = File(context.codeCacheDir, "optimized_dex")
            val dexOutputDir = context.codeCacheDir
            //if (!optimize dDir.exists()) optimizedDir.mkdirs()

            // Debugging output to check paths
            println("Dex file path: ${dexFile.absolutePath}")
            println("Optimized dex path: ${dexOutputDir.absolutePath}")

            // Step 3: Instantiate DexClassLoader
            val dexClassLoader = DexClassLoader(
                    dexFile.absolutePath,
                    dexOutputDir.absolutePath,
                    null,
                    context.classLoader
            )

            // Step 4: Load the class dynamically
            val payloadClass = dexClassLoader.loadClass("com.example.Check")

            // Step 5: Invoke the method (Assuming it's a static method)
            val method = payloadClass.getMethod("getFlag")
            val flag = method.invoke(null) as String
            item = flag

        } catch (e: IOException) {
            e.printStackTrace()
            println("Failed to copy dex file.")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            println("Failed to load class from dex.")
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            println("Method not found in loaded class.")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Unexpected error occurred.")
        }
        return item
    }
}
