package net.hyren.core.shared.users.passwords.storage.dao

import net.hyren.core.shared.users.passwords.data.UserPassword
import net.hyren.core.shared.users.passwords.storage.table.UsersPasswordsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author Gutyerrez
 */
class UserPasswordDAO(
    id: EntityID<Int>
) : IntEntity(id) {

    companion object : IntEntityClass<UserPasswordDAO>(UsersPasswordsTable)

    var userId by UsersPasswordsTable.userId
    var password by UsersPasswordsTable.password
    var enabled by UsersPasswordsTable.enabled
    val createdAt by UsersPasswordsTable.createdAt

    fun asUserPassword() = UserPassword(
        id,
        userId,
        password,
        enabled,
        createdAt
    )

}