package com.redefantasy.core.shared.users.passwords.storage.dao

import com.redefantasy.core.shared.users.passwords.data.UserPassword
import com.redefantasy.core.shared.users.passwords.storage.table.UserPasswordTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author Gutyerrez
 */
class UserPasswordDAO(
    id: EntityID<Int>
) : IntEntity(id) {

    companion object : IntEntityClass<UserPasswordDAO>(UserPasswordTable)

    var userId by UserPasswordTable.userId
    var password by UserPasswordTable.password
    var enabled by UserPasswordTable.enabled
    val createdAt by UserPasswordTable.createdAt

    fun asUserPassword() = UserPassword(
        id,
        userId,
        password,
        enabled,
        createdAt
    )

}