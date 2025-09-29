package cl.jlopezr.multiplatform.core.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

object TaskActionNotificationManager {
    
    private const val TAG = "TaskActionNotificationManager"
    private const val CHANNEL_ID = "task_action_channel"
    private const val CHANNEL_NAME = "Acciones de Tarea"
    private const val NOTIFICATION_ID = 2001
    
    fun showTaskActionNotification(
        context: Context,
        taskId: String
    ) {
        Log.d(TAG, "Mostrando notificación simple para tarea: $taskId")
        
        // Crear canal de notificación si es necesario
        createNotificationChannel(context)
        
        // Crear intent para abrir la app normalmente (sin extras especiales)
        val openAppIntent = Intent(context, cl.jlopezr.multiplatform.MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("view_task_from_notification", taskId) // Solo pasamos el ID de la tarea
        }
        
        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            taskId.hashCode(),
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Crear la notificación simple con solo "Ver tarea"
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Recordatorio de Tarea")
            .setContentText("Presiona para completar la tarea (Recuerda que la tarea aparecera en completadas)")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(openAppPendingIntent)
            .addAction(
                android.R.drawable.ic_menu_view,
                "completar tarea",
                openAppPendingIntent
            )
            .build()
        
        // Mostrar la notificación
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
        
        Log.d(TAG, "Notificación simple mostrada exitosamente")
    }
    
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para acciones de tareas después de alarmas"
                enableVibration(true)
                enableLights(true)
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            
            Log.d(TAG, "Canal de notificación creado: $CHANNEL_ID")
        }
    }
    
    fun cancelTaskActionNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
        Log.d(TAG, "Notificación de acción cancelada")
    }
}