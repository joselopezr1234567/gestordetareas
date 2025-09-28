package cl.jlopezr.multiplatform.core.notification

actual object NotificationManagerFactory {
    actual fun create(): NotificationManager {
        return IOSNotificationManager()
    }
}