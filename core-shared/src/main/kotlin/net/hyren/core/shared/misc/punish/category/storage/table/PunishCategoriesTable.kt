package net.hyren.core.shared.misc.punish.category.storage.table

import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.misc.exposed.array
import net.hyren.core.shared.misc.punish.durations.PunishDuration
import net.hyren.core.shared.providers.databases.mariadb.dao.StringTable

/**
 * @author SrGutyerrez
 **/
object PunishCategoriesTable : StringTable("punish_categories") {

    val displayName = varchar("display_name", 255)
    val description = varchar("description", 255)
    val punishDurations = array<PunishDuration>("punish_durations", Array<PunishDuration>::class)
    val group = enumerationByName("group_name", 255, Group::class)
    val enabled = bool("enabled")

}