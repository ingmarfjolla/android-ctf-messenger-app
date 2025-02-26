package com.example.myapplication.password

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class PasswordHandler {

    fun generateSecretKey(customSecretKey: String): SecretKeySpec {
        val keyBytes = customSecretKey.toByteArray(Charsets.UTF_8)
        val sha = MessageDigest.getInstance("SHA-256")
        val hash = sha.digest(keyBytes)
        return SecretKeySpec(hash, "AES")
    }
    fun encrypt(
        textToEncrypt: String,
        secretKey: SecretKey,
    ): String {
        val plainText = textToEncrypt.toByteArray()
        val fixedIV = "1234567890123456".toByteArray(Charsets.UTF_8)
        val ivSpec = IvParameterSpec(fixedIV)

        val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,ivSpec)

        val encrypt = cipher.doFinal(plainText)
        return Base64.encodeToString(encrypt, Base64.DEFAULT)
    }



}