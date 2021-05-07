package net.hyren.core.shared.users.storage.dto

import java.util.*

/**
 * @author SrGutyerrez
 **/
class CreateUserDTO(
        val id: UUID,
        val name: String,
        val lastAddress: String
)