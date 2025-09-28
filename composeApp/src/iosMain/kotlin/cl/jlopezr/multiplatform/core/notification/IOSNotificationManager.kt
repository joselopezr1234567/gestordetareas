package cl.jlopezr.multiplatform.core.notification

import kotlinx.datetime.LocalDateTime

class IOSNotificationManager : NotificationManager {
    
    override suspend fun scheduleNotification(
        id: String,
        title: String,
        message: String,
        dateTime: LocalDateTime
    ): Result<Unit> {
        // TODO: Implementar notificaciones para iOS
        return Result.success(Unit)
    }
    
    override suspend fun cancelNotification(id: String): Result<Unit> {
        // TODO: Implementar cancelación para iOS
        return Result.success(Unit)
    }
    
    override suspend fun areNotificationsEnabled(): Boolean {
        // TODO: Verificar permisos en iOS
        return true
    }
    
    override suspend fun requestNotificationPermission(): Boolean {
        // TODO: Solicitar permisos en iOS
        return true
    }
}