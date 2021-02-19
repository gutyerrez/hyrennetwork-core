package com.redefantasy.core.shared.users.storage.dto

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import java.util.function.Consumer

/**
 * @author SrGutyerrez
 **/
class UpdateUserByIdDTO<E: Entity<*>>(
        val id: EntityID<UUID>,
        val execute: Consumer<E>
)