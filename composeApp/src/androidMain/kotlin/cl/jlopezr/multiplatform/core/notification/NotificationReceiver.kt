package cl.jlopezr.multiplatform.core.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import cl.jlopezr.multiplatform.MainActivity

class NotificationReceiver : BroadcastReceiver() {
    
    companion object {
        private const val CHANNEL_ID = "task_reminders"
        private const val TAG = "NotificationReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "NotificationReceiver.onReceive llamado")
        Log.d(TAG, "Intent action: ${intent.action}")
        Log.d(TAG, "Intent extras: ${intent.extras}")
        
        val notificationId = intent.getStringExtra("notification_id")
        val title = intent.getStringExtra("title") ?: "Recordatorio de Tarea"
        val message = intent.getStringExtra("message") ?: "Tienes una tarea pendiente"
        
        Log.d(TAG, "ID: $notificationId, Título: $title, Mensaje: $message")
        
        if (notificationId == null) {
            Log.e(TAG, "notification_id es null, no se puede mostrar la notificación")
            return
        }
        
        showNotification(context, notificationId, title, message)
    }
    
    private fun showNotification(context: Context, id: String, title: String, message: String) {
        Log.d(TAG, "Mostrando notificación - ID: $id, Título: $title, Mensaje: $message")
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 250, 250, 250))
            .build()
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = id.hashCode()
        
        Log.d(TAG, "Enviando notificación con ID: $notificationId")
        notificationManager.notify(notificationId, notification)
        Log.d(TAG, "Notificación enviada exitosamente")
    }
}