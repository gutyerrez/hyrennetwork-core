package net.hyren.core.shared.misc.punish.durations

import com.fasterxml.jackson.annotation.JsonProperty
import net.hyren.core.shared.misc.punish.PunishType

/**
 * @author SrGutyerrez
 **/
data class PunishDuration(
        @JsonProperty val duration: Long,
        @JsonProperty("punish_type") val punishType: PunishType
)