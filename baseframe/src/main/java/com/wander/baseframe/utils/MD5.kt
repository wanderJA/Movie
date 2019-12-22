package com.wander.baseframe.utils

import java.security.MessageDigest

object MD5 {

    /**
     * md5
     *
     * @param str
     * @return
     */
    fun md5(str: String): String {
        try {
            val messageDigest = MessageDigest.getInstance("MD5")
            messageDigest.reset()
            messageDigest.update(str.toByteArray(charset("UTF-8")))
            val byteArray = messageDigest.digest()
            val sb = StringBuilder()
            for (i in byteArray.indices) {
                if (Integer.toHexString(0xFF and byteArray[i].toInt()).length == 1) {
                    sb.append("0").append(Integer.toHexString(0xFF and byteArray[i].toInt()))
                } else {
                    sb.append(Integer.toHexString(0xFF and byteArray[i].toInt()))
                }
            }

            return sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return str
    }

    fun toMd5(var0: ByteArray, var1: Boolean): String {
        try {
            val var2 = MessageDigest.getInstance("MD5")
            var2.reset()
            var2.update(var0)
            return toHexString(var2.digest(), "", var1)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun toHexString(var0: ByteArray, var1: String, var2: Boolean): String {
        val var3 = StringBuilder()
        val var5 = var0.size

        for (var6 in 0 until var5) {
            val var7 = var0[var6]
            var var8 = Integer.toHexString(255 and var7.toInt())
            if (var2) {
                var8 = var8.toUpperCase()
            }

            if (var8.length == 1) {
                var3.append("0")
            }

            var3.append(var8).append(var1)
        }

        return var3.toString()
    }
}
