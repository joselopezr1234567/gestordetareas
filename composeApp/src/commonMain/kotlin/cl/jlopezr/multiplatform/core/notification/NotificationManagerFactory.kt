package cl.jlopezr.multiplatform.core.notification

/**
 * Factory para crear instancias del NotificationManager según la plataforma
 */
expect object NotificationManagerFactory {
    fun create(): NotificationManager
}