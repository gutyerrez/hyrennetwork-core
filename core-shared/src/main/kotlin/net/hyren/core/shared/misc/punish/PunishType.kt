package net.hyren.core.shared.misc.punish

/**
 * @author SrGutyerrez
 **/
enum class PunishType(
    val sampleName: String,
    val displayName: String
) {

    BAN("permanentemente banido", "banido"),
    TEMP_BAN("temporariamente banido", "banido"),
    MUTE("silenciado", "silenciado")

}