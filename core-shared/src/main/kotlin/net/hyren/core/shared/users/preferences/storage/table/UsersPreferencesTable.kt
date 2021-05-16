package net.hyren.core.shared.users.preferences.storage.table

import net.hyren.core.shared.misc.exposed.array
import net.hyren.core.shared.misc.preferences.data.Preference
import net.hyren.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.sql.Table

/**
 * @author Gutyerrez
 */
object UsersPreferencesTable : Table("users_preferences") {

    var userId = reference("user_id", UsersTable)
    var preferences = array<Preference>("preferences", Array<Preference>::class)

}