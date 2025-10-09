package cl.jlopezr.multiplatform.core.notification

import kotlinx.datetime.LocalDateTime

class IOSNotificationManager : NotificationManager {
    
    override suspend fun scheduleNotification(
        id: String,
        title: String,
        message: String,
        dateTime: LocalDateTime
    ): Result<Unit> {

        return Result.success(Unit)
    }
    
    override suspend fun cancelNotification(id: String): Result<Unit> {

        return Result.success(Unit)
    }
    
    override suspend fun areNotificationsEnabled(): Boolean {

        return true
    }
    
    override suspend fun requestNotificationPermission(): Boolean {

        return true
    }
}