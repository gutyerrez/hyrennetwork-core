package com.redefantasy.core.shared.world.location

import java.util.function.Function

/**
 * @author SrGutyerrez
 **/
interface LocationParser<T> : Function<SerializedLocation, T>