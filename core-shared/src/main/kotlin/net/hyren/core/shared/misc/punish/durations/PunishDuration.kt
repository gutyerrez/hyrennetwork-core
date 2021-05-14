package net.hyren.core.shared.misc.punish.durations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.hyren.core.shared.misc.punish.PunishType

/**
 * @author SrGutyerrez
 **/
@Serializable
data class PunishDuration(
    val duration: Long,
    @SerialName("punish_type")
    val punishType: PunishType
)