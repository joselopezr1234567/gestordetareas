package cl.jlopezr.multiplatform.core.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager as AndroidNotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import cl.jlopezr.multiplatform.core.notification.NotificationManager

class AndroidNotificationManager(
    private val context: Context
) : NotificationManager {
    
    companion object {
        private const val CHANNEL_ID = "task_reminders"
        private const val CHANNEL_NAME = "Recordatorios de Tareas"
        private const val CHANNEL_DESCRIPTION = "Notificaciones para recordatorios de tareas"
        private const val TAG = "AndroidNotificationManager"
    }
    
    private val notificationManager = NotificationManagerCompat.from(context)
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    
    init {
        createNotificationChannel()
    }
    
    override suspend fun scheduleNotification(
        id: String,
        title: String,
        message: String,
        dateTime: LocalDateTime
    ): Result<Unit> {
        return try {
            Log.d(TAG, "Programando notificación - ID: $id, Título: $title, Fecha: $dateTime")
            
            // Verificar permisos
            if (!areNotificationsEnabled()) {
                Log.w(TAG, "Las notificaciones no están habilitadas")
                return Result.failure(SecurityException("Las notificaciones no están habilitadas"))
            }
            
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra("notification_id", id)
                putExtra("title", title)
                putExtra("message", message)
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            val triggerTime = dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            val currentTime = System.currentTimeMillis()
            
            Log.d(TAG, "Tiempo actual: $currentTime, Tiempo de activación: $triggerTime")
            Log.d(TAG, "Diferencia: ${triggerTime - currentTime} ms")
            
            if (triggerTime <= currentTime) {
                Log.w(TAG, "La fecha de notificación es en el pasado")
                return Result.failure(IllegalArgumentException("La fecha de notificación debe ser en el futuro"))
            }
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
                Log.d(TAG, "Alarma programada con setExactAndAllowWhileIdle")
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
                Log.d(TAG, "Alarma programada con setExact")
            }
            
            Log.d(TAG, "Notificación programada exitosamente")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error al programar notificación", e)
            Result.failure(e)
        }
    }
    
    override suspend fun cancelNotification(id: String): Result<Unit> {
        return try {
            val intent = Intent(context, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            alarmManager.cancel(pendingIntent)
            notificationManager.cancel(id.hashCode())
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun areNotificationsEnabled(): Boolean {
        return notificationManager.areNotificationsEnabled()
    }
    
    override suspend fun requestNotificationPermission(): Boolean {
        // En Android 13+ se requiere permiso explícito
        // Este método debería ser llamado desde la Activity
        return areNotificationsEnabled()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                AndroidNotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                enableLights(true)
            }
            
            val systemNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as AndroidNotificationManager
            systemNotificationManager.createNotificationChannel(channel)
        }
    }
}