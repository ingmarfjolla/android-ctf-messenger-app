package com.example.myapplication.emulator.fingerprintjs

import android.os.Build
import java.io.File
import java.util.Scanner


public data class CpuInfo(
    val commonInfo: List<Pair<String, String>>,
    // except processor : x pairs. index in list may be considered as an index of a processor.
    val perProcessorInfo: List<List<Pair<String, String>>>,
) {
    public companion object {
        public val EMPTY: CpuInfo = CpuInfo(
            commonInfo = emptyList(),
            perProcessorInfo = emptyList(),
        )
    }
}

public interface CpuInfoProvider {
    public fun cpuInfo(): Map<String, String>
    public fun cpuInfoV2(): CpuInfo
    public fun abiType(): String
    public fun coresCount(): Int
}

internal class CpuInfoProviderImpl :
    CpuInfoProvider {
    override fun cpuInfo(): Map<String, String> {
        return getCpuInfo()
    }

    override fun cpuInfoV2(): CpuInfo {
        return getCpuInfoV2()
    }

    override fun abiType(): String {
        return Build.SUPPORTED_ABIS[0]!!
    }

    override fun coresCount(): Int {
        return Runtime.getRuntime().availableProcessors()
    }
    private fun getCpuInfo(): Map<String, String> {
        try {
            val file = File(CPU_INFO_PATH)
            if (!file.exists() || !file.canRead()) {
                // File doesn't exist or isn't readable; return an empty map.
                return emptyMap()
            }
            val map = mutableMapOf<String, String>()
            Scanner(file).use { scanner ->
                while (scanner.hasNextLine()) {
                    val line = scanner.nextLine()
                    if (line.isNotBlank()) {
                        val cpuInfoValues = line.split(KEY_VALUE_DELIMITER)
                        if (cpuInfoValues.size > 1) {
                            map[cpuInfoValues[0].trim()] = cpuInfoValues[1].trim()
                        }
                    }
                }
            }
            return map
        } catch (e: Exception) {
            // Optionally log the exception.
            return emptyMap()
        }
    }
//    private fun getCpuInfo(): Map<String, String> {
//        val file = File(CPU_INFO_PATH)
//        if (!file.exists()) {
//            // File not available, return an empty map.
//            return emptyMap()
//        }
//        val map: MutableMap<String, String> = HashMap()
//        Scanner(File(CPU_INFO_PATH)).use { s ->
//            while (s.hasNextLine()) {
//                val cpuInfoValues = s.nextLine()!!.split(KEY_VALUE_DELIMITER)
//                if (cpuInfoValues.size > 1) map[cpuInfoValues[0].trim { it <= ' ' }] =
//                    cpuInfoValues[1].trim { it <= ' ' }
//            }
//        }
//
//        return map
//    }
    // Reads /proc/cpuinfo, splits it into blocks (separated by blank lines),
    // and then treats blocks containing a "processor" key as per-processor info.
    // Other blocks are merged into common info (only keys that have the same value across blocks are kept).
    private fun getCpuInfoV2(): CpuInfo {
        val file = File(CPU_INFO_PATH)
        if (!file.exists()) return CpuInfo.EMPTY

        val content = file.readText()
        // Split blocks on blank lines (allowing for spaces)
        val blocks = content.split(Regex("\\n\\s*\\n"))
        val perProcessorInfo = mutableListOf<List<Pair<String, String>>>()
        var commonMap: MutableMap<String, String>? = null

        for (block in blocks) {
            if (block.isBlank()) continue
            val lines = block.lines().filter { it.isNotBlank() }
            val info = lines.mapNotNull { line ->
                val parts = line.split(KEY_VALUE_DELIMITER)
                if (parts.size < 2) null else parts[0].trim() to parts[1].trim()
            }
            if (info.isNotEmpty()) {
                if (info.any { it.first.equals("processor", ignoreCase = true) }) {
                    perProcessorInfo.add(info)
                } else {
                    if (commonMap == null) {
                        commonMap = info.toMap().toMutableMap()
                    } else {
                        // Keep only keys that have the same value in every common block.
                        for ((key, value) in info) {
                            if (commonMap[key] != value) {
                                commonMap.remove(key)
                            }
                        }
                    }
                }
            }
        }
        val commonInfo = commonMap?.toList() ?: emptyList()
        return CpuInfo(commonInfo = commonInfo, perProcessorInfo = perProcessorInfo)
    }

//    private fun getCpuInfoV2(): String {
//        val cpuInfoContents = File(CPU_INFO_PATH).readText()
//        return cpuInfoContents
//    }
}

private const val CPU_INFO_PATH = "/proc/cpuinfo"
private const val KEY_VALUE_DELIMITER = ": "