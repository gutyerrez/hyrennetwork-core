package com.redefantasy.core.shared.users.preferences.storage.table

import com.redefantasy.core.shared.misc.exposed.array
import com.redefantasy.core.shared.misc.preferences.Preference
import com.redefantasy.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.sql.Table

/**
 * @author Gutyerrez
 */
object UsersPreferencesTable : Table("users_preferences") {

    var userId = reference("user_id", UsersTable)
    var preferences = array<Preference>("preferences", Array<Preference>::class)

}