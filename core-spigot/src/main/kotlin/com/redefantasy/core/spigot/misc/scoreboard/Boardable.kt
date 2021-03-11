package com.redefantasy.core.spigot.misc.scoreboard

/**
 * @author Gutyerrez
 */
interface Boardable {

    fun set(
        score: Int,
        text: String
    )

    fun setTitle(title: String)

    fun reset(score: Int)

}