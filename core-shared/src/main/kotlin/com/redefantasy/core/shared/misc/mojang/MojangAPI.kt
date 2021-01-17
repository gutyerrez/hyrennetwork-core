package com.redefantasy.core.shared.misc.mojang

import com.fasterxml.jackson.annotation.JsonProperty
import com.redefantasy.core.shared.CoreConstants
import okhttp3.Request
import java.util.*

/**
 * @author SrGutyerrez
 **/
object MojangAPI {

    fun fetchByName(name: String): GameProfile? {
        val request = Request.Builder()
                .url("https://api.mojang.com/users/profiles/minecraft/Gutyerrez")
                .header("Accept", "application/json")
                .get()
                .build()

        val response = CoreConstants.OK_HTTP.newCall(request)
                .execute()
                .body

        return CoreConstants.JACKSON.readValue(
                response?.string(),
                GameProfile::class.java
        )
    }

    data class GameProfile(
            @JsonProperty("id") val id: String,
            @JsonProperty("name") val name: String
    ) {

        fun getUniqueId(): UUID {
            return UUID.fromString(
                    this.id.replace(
                            "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                            "$1-$2-$3-$4-$5"
                    )
            )
        }

    }

}