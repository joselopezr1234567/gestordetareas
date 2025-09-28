package cl.jlopezr.multiplatform.core.notification

import kotlinx.datetime.LocalDateTime

/**
 * Interfaz para el manejo de notificaciones multiplataforma
 */
interface NotificationManager {
    
    /**
     * Programa una notificación para una fecha y hora específica
     * 
     * @param id Identificador único de la notificación
     * @param title Título de la notificación
     * @param message Mensaje de la notificación
     * @param dateTime Fecha y hora cuando debe mostrarse la notificación
     */
    suspend fun scheduleNotification(
        id: String,
        title: String,
        message: String,
        dateTime: LocalDateTime
    ): Result<Unit>
    
    /**
     * Cancela una notificación programada
     * 
     * @param id Identificador de la notificación a cancelar
     */
    suspend fun cancelNotification(id: String): Result<Unit>
    
    /**
     * Verifica si las notificaciones están habilitadas
     */
    suspend fun areNotificationsEnabled(): Boolean
    
    /**
     * Solicita permisos de notificación al usuario
     */
    suspend fun requestNotificationPermission(): Boolean
}