package com.redefantasy.core.spigot.misc.spawn.repositories

import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.shared.world.location.SerializedLocation

/**
 * @author Gutyerrez
 */
interface ISpawnRepository : IRepository {

    fun fetch(): SerializedLocation?

}