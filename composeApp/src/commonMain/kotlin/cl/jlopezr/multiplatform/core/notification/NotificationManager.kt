package cl.jlopezr.multiplatform.core.notification

import kotlinx.datetime.LocalDateTime


interface NotificationManager {
    

    suspend fun scheduleNotification(
        id: String,
        title: String,
        message: String,
        dateTime: LocalDateTime
    ): Result<Unit>
    

    suspend fun cancelNotification(id: String): Result<Unit>
    

    suspend fun areNotificationsEnabled(): Boolean
    

    suspend fun requestNotificationPermission(): Boolean
}