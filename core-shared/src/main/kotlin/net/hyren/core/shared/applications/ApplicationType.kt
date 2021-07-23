package net.hyren.core.shared.applications

/**
 * @author SrGutyerrez
 **/
enum class ApplicationType {

    PROXY,

    LOBBY,
    
    PUNISHED_LOBBY,
    
    SERVER_SPAWN,
    SERVER_VIP,
    SERVER_WORLD,
    SERVER_WORLD_NORTH,
    SERVER_WORLD_EAST,
    SERVER_WORLD_WEST,
    SERVER_WORLD_SOUTH,
    SERVER_END,
    SERVER_MINE,
    SERVER_ARENA,
    SERVER_WAR,
    
    SERVER_TESTS,
    
    DUNGEON,
    BUILD,
    GENERIC;
    
    fun isTerrain() = arrayOf(
        SERVER_WORLD,
        SERVER_WORLD_NORTH,
        SERVER_WORLD_EAST,
        SERVER_WORLD_WEST,
        SERVER_WORLD_SOUTH
    ).contains(this)

}