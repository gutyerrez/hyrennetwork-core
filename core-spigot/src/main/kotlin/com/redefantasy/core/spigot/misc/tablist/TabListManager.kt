package com.redefantasy.core.spigot.misc.tablist

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.spigot.CoreSpigotConstants
import com.redefantasy.core.spigot.misc.plugin.CustomPlugin
import com.redefantasy.core.spigot.misc.scoreboard.bukkit.GroupScoreboard
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo
import org.bukkit.Bukkit
import java.util.function.Function

/**
 * @author Gutyerrez
 */
class TabListManager(private val plugin: CustomPlugin) {

    private lateinit var GET_BOARD: Function<User, GroupScoreboard>

    fun enable() {

        this.registerProtocolListeners()
    }

    fun startUpdaterTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(
            plugin,
            {
                //
            },
            0,
            20 * 5
        )
    }

    fun registerProtocolListeners() {
        CoreSpigotConstants.PROTOCOL.registerListener(
            object : TinyProtocol.PacketListener(0) {
                override fun onSent(event: TinyProtocol.PacketEvent) {
                    val packet = event.packet

                    if (packet is PacketPlayOutPlayerInfo) {
                        if (packet.a == PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER) {
                            Bukkit.getScheduler().runTaskLater(
                                plugin,
                                {
                                    if (event.player !== null && event.player!!.isOnline) {
                                        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(event.player!!.uniqueId)

                                        Thread {
                                            packet.b.forEach {
                                                val _user = CoreProvider.Cache.Local.USERS.provide().fetchById(it.a().id)

                                                if (_user !== null) {
                                                    //
                                                }
                                            }
                                        }.start()
                                    }
                                },
                                5L
                            )
                        }
                    }
                }
            }
        )
    }

}