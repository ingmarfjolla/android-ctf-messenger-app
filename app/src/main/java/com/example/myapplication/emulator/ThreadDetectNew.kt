package com.example.myapplication.emulator

import android.opengl.GLES20
import android.util.Log
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ThreadDetectNew {

    fun getThreadInfo(): String {
        val sb = StringBuilder()
        try {
            // Get list of thread IDs from /proc/self/task
            val taskDir = File("/proc/self/task/")
            val threadIds = taskDir.list() ?: return "No threads found."

            sb.append("=== /proc/self/task Thread Information ===\n")
            // Iterate over threads and log basic info (ID, name, status, stack trace)
            for (tid in threadIds) {
                val commPath = "/proc/self/task/$tid/comm"
                val threadName = File(commPath).takeIf { it.exists() }?.readText()?.trim() ?: "N/A"
                sb.append("Thread ID: $tid\n")
                sb.append("Thread Name: $threadName\n")
            }

            val representativeTid = threadIds.firstOrNull() ?: "0"
            sb.append("=== File Descriptors (from thread $representativeTid) ===\n")
            sb.append(inspectFileDescriptors(representativeTid))
            sb.append("\n")
            sb.append("=== End of Java Thread Information ===\n")
        } catch (e: Exception) {
            sb.append("Error: ${e.message}")
        }
        return sb.toString()
    }

    private fun inspectFileDescriptors(tid: String): String {
        val sb = StringBuilder()
        val fdDir = File("/proc/self/task/$tid/fd/")
        if (!fdDir.exists() || !fdDir.isDirectory) {
            return "FD directory not accessible for thread $tid\n"
        }

        sb.append("=== File Descriptors for Thread $tid ===\n")
        fdDir.listFiles()?.forEach { fd ->
            try {
                val target = fd.canonicalPath
                sb.append("FD: ${fd.name} -> $target\n")
            } catch (e: Exception) {
                sb.append("Error accessing FD ${fd.name}: ${e.message}\n")
            }
        }
        return sb.toString()
    }

    fun logOpenGLESInfo() {
        val glVersion = GLES20.glGetString(GLES20.GL_VERSION)
        Log.d("OpenGLES", "GL_VERSION: $glVersion")

        val glRenderer = GLES20.glGetString(GLES20.GL_RENDERER)
        Log.d("OpenGLES", "GL_RENDERER: $glRenderer")

        val glExtensions = GLES20.glGetString(GLES20.GL_EXTENSIONS)
        Log.d("OpenGLES", "GL_EXTENSIONS: $glExtensions")
    }

    private fun intToBytes(i: Int): ByteArray =
        ByteBuffer.allocate(Int.SIZE_BYTES).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array()

    // The interactWithGoldfishPipe() method remains unchanged.
    private fun interactWithGoldfishPipe(): String {
        val pipePath = "/dev/goldfish_pipe"
        val serviceName = "pipe:GLProcessPipe" + '\u0000' // Append a null character

        val sb = StringBuilder()

        val pipeFile = File(pipePath)
        if (!pipeFile.exists()) {
            return "Pipe $pipePath does not exist on this device.\n"
        }

        try {
            val pipe = RandomAccessFile(pipePath, "rw")
            Log.d("GoldfishPipe", "Opened $pipePath successfully.")
            sb.append("Opened $pipePath successfully.\n")

            // Write the service name
            pipe.writeBytes(serviceName)
            sb.append("Wrote service name: $serviceName\n")
            Log.d("GoldfishPipe", "Wrote service name: $serviceName")

            val OP_rcSelectChecksumHelper = 10028
            val newProtocol = 1 // Example value
            val reserved = 0 // Reserved field
            val sizeWithoutChecksum = 8 + 4 + 4 // Opcode + size + 2 fields
            val totalSize = sizeWithoutChecksum

            // Construct the request buffer
            val requestBuffer = ByteBuffer.allocate(totalSize)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(OP_rcSelectChecksumHelper) // Opcode
                .putInt(totalSize) // Total size
                .putInt(newProtocol) // newProtocol
                .putInt(reserved) // reserved

            pipe.write(requestBuffer.array())
            Log.d("GoldfishPipe", "Request (hex): ${requestBuffer.array().joinToString(" ") { it.toString(16) }}")
            sb.append("Sent rcSelectChecksumHelper command with protocol $newProtocol.\n")

            val buffer = ByteArray(2056)
            val bytesRead = pipe.read(buffer)
            if (bytesRead > 0) {
                val response = buffer.decodeToString(0, bytesRead)
                sb.append("Response: $response\n")
                Log.d("GoldfishPipe", "Read response: $response")
                val responseInt = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).int
                sb.append("Interpreted response as int: $responseInt\n")
            } else {
                sb.append("No data received from pipe.\n")
                Log.d("GoldfishPipe", "No data received from pipe.")
            }

            pipe.close()
            Log.d("GoldfishPipe", "Closed the pipe.")
        } catch (e: Exception) {
            sb.append("Error interacting with $pipePath: ${e.message}\n")
            Log.e("GoldfishPipe", "Error interacting with $pipePath: ${e.message}")
        }
        return sb.toString()
    }
}
