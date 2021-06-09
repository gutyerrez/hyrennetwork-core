package net.hyren.core.shared.misc.annotations

import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.TYPE

/**
 * @author Gutyerrez
 */
@MustBeDocumented
@Retention(SOURCE)
@Target(CLASS, CONSTRUCTOR, FIELD, FUNCTION, TYPE)
annotation class Beta