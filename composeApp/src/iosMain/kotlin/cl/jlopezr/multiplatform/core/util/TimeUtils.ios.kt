package cl.jlopezr.multiplatform.core.util

import platform.posix.gettimeofday
import platform.posix.timeval
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * Implementación para iOS de getCurrentTimeMillis
 */
@OptIn(ExperimentalForeignApi::class)
actual fun getCurrentTimeMillis(): Long = memScoped {
    val timeVal = alloc<timeval>()
    gettimeofday(timeVal.ptr, null)
    (timeVal.tv_sec * 1000L) + (timeVal.tv_usec / 1000L)
}