package com.redefantasy.core.shared.users.storage.dto

import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class UpdateUserByIdDTO<E>(
        val id: EntityID<UUID>,
        val execute: (e: E) -> Unit
)