package com.redefantasy.core.shared.users.punishments.storage.dto

import org.jetbrains.exposed.dao.Entity
import java.util.function.Consumer

/**
 * @author SrGutyerrez
 **/
class UpdateUserPunishmentByIdDTO(
        val id: Int,
        val execute: Consumer<Entity<*>>
)