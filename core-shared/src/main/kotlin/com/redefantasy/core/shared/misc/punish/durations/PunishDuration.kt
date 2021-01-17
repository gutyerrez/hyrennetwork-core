package com.redefantasy.core.shared.misc.punish.durations

import com.fasterxml.jackson.annotation.JsonProperty
import com.redefantasy.core.shared.misc.punish.PunishType

/**
 * @author SrGutyerrez
 **/
data class PunishDuration(
        @JsonProperty val duration: Long,
        @JsonProperty("punish_type") val punishType: PunishType
)