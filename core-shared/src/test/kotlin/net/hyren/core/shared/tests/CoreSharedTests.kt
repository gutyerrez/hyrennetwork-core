package net.hyren.core.shared.tests

import net.hyren.core.shared.misc.json.KJson
import net.hyren.core.shared.misc.punish.PunishType
import net.hyren.core.shared.misc.punish.durations.PunishDuration

/**
 * @author Gutyerrez
 */
fun main() {
    val array = arrayOf(
        PunishDuration(
            1000,
            PunishType.MUTE
        ),PunishDuration(
            -1,
            PunishType.BAN
        )
    )

    val encoded = KJson.encodeToString(array)

    println(encoded)

    val decoded = KJson.decodeFromString(Array<PunishDuration>::class, encoded)

    println(decoded)
}