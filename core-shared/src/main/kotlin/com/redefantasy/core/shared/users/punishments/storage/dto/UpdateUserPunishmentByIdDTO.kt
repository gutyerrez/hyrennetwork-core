package com.redefantasy.core.shared.users.punishments.storage.dto

/**
 * @author SrGutyerrez
 **/
class UpdateUserPunishmentByIdDTO<E>(
        val id: Int,
        val execute: (e: E) -> Unit
)