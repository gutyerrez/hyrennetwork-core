package com.redefantasy.core.shared.wrapper

import net.md_5.bungee.api.chat.BaseComponent

/**
 * @author Gutyerrez
 */
interface Wrapper<T> {

    fun sendMessage(senderName: String, message: String)

    fun sendMessage(senderName: String, message: BaseComponent)

    fun sendMessage(senderName: String, messages: Array<BaseComponent>)


}