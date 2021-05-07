package net.hyren.core.shared.misc.utils

import java.security.MessageDigest

/**
 * @author Gutyerrez
 */
object EncryptionUtil {

    fun hash(type: Type, text: String): String {
        when (type) {
            Type.SHA256 -> return MessageDigest.getInstance(type.instance)
                .digest(text.toByteArray(Charsets.UTF_8))
                .fold("", { str, it -> str + "%02x".format(it) })
            else -> return ""
        }
    }

    enum class Type(val instance: String) {

        SHA256("SHA-256")

    }

}