package com.redefantasy.core.shared.users.storage.dto

import java.util.*

/**
 * @author SrGutyerrez
 **/
class UpdateUserByIdDTO<E>(
        val id: UUID,
        val execute: (e: E) -> Unit
)