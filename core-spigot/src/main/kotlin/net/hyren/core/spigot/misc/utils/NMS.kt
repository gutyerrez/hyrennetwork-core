package net.hyren.core.spigot.misc.utils

import net.hyren.core.spigot.misc.minecraft.entity.exceptions.EntitySpawnException
import net.minecraft.server.v1_8_R3.Entity
import net.minecraft.server.v1_8_R3.World
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.event.entity.CreatureSpawnEvent
import java.util.function.Consumer
import kotlin.reflect.KClass

/**
 * @author Gutyerrez
 */
object NMS {

    fun <T: Entity> spawnCustomEntity(
        customEntityKClass: KClass<T>,
        entityClass: KClass<out Entity>,
        location: Location,
        preSpawn: Consumer<Entity>? = null
    ): T {
        val worldServer = (location.world as CraftWorld).handle

        val customEntity = customEntityKClass.java.getConstructor(World::class.java).newInstance(worldServer)

        customEntity.setLocation(
            location.x,
            location.y,
            location.z,
            location.yaw,
            location.pitch
        )
        customEntity.setPositionRotation(
            location.x,
            location.y,
            location.z,
            location.yaw,
            location.pitch
        )

        if (preSpawn !== null) preSpawn.accept(customEntity)

        if (!worldServer.addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM))
            throw EntitySpawnException("Could not spawn custom entity ${customEntityKClass.java.name}")

        return customEntity
    }

    /*fun registerEntityType(
        customEntityKClass: KClass<out Entity>,
        name: String,
        id: Int
    ) {
    }*/

    /*fun clearEntityType(
        inName: String,
        inId: Int
    ) {
        val cMap = this.getField(
            EntityTypes::class,
            "c"
        ) as Map<String?, Class<Entity>>
        val eMap = this.getField(
            EntityTypes::class,
            "e"
        ) as Map<Int?, Class<out Entity>>

        (cMap[null] as Map<*, *>?).remove(inName)
        (eMap[null] as MutableMap<*, *>?)!!.remove(inID)
    }*/

    /*fun getNMSClassByName(className: String) = Class.forName("net.minecraft.server.${this.getMinecraftRevision()}.$className")*/

    /*fun getMinecraftRevision() = Bukkit.getServer()::class.java.`package`.name.replace("org.bukkit.craftbukkit.", "").split(".")[0]*/

    /*fun getField(
        inSource: KClass<*>,
        inField: String
    ): Field {
        val field = inSource.java.getDeclaredField(inField)

        field.isAccessible = true

        return field
    }*/

}