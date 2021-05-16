package net.hyren.core.shared.tests

import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.misc.json.KJson
import net.hyren.core.shared.misc.punish.PunishType
import net.hyren.core.shared.misc.punish.category.data.PunishCategory
import net.hyren.core.shared.misc.punish.category.storage.table.PunishCategoriesTable
import net.hyren.core.shared.misc.punish.durations.PunishDuration
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author Gutyerrez
 */
fun main() {
    val array = arrayOf(
        PunishCategory(
            EntityID(
                "test",
                PunishCategoriesTable
            ),
            "test orra",
            "lalalala",
            arrayOf(
                PunishDuration(
                    1000,
                    PunishType.MUTE
                )
            ),
            Group.DEFAULT,
            true
        )
    )

    val encoded = KJson.encodeToString(array)

    println(encoded)

    val decoded = KJson.decodeFromString(Array<PunishCategory>::class, encoded)

    println(decoded)
}