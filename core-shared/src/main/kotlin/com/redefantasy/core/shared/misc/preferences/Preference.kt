package com.redefantasy.core.shared.misc.preferences

import com.redefantasy.core.shared.misc.minecraft.material.Material
import java.io.Serializable

/**
 * @author SrGutyerrez
 **/
interface Preference {

    val name: String
    var preferenceState: PreferenceState

    fun getIcon(): PreferenceIcon

}

open class PreferenceIcon(
    val material: Material,
    val displayName: String,
    val lore: Array<String> = emptyArray()
) : Serializable