package cl.jlopezr.multiplatform.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "AlarmReceiver"
        const val EXTRA_TASK_ID = "task_id"
        const val EXTRA_TASK_TITLE = "task_title"
        const val EXTRA_TASK_MESSAGE = "task_message"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "AlarmReceiver.onReceive llamado")
        Log.d(TAG, "Intent action: ${intent.action}")
        Log.d(TAG, "Intent extras: ${intent.extras}")
        
        val taskId = intent.getStringExtra(EXTRA_TASK_ID)
        val title = intent.getStringExtra(EXTRA_TASK_TITLE) ?: "Recordatorio de Tarea"
        val message = intent.getStringExtra(EXTRA_TASK_MESSAGE) ?: "Tienes una tarea pendiente"
        
        Log.d(TAG, "ID: $taskId, Título: $title, Mensaje: $message")
        
        if (taskId == null) {
            Log.e(TAG, "task_id es null, no se puede iniciar la alarma")
            return
        }
        
        // Iniciar el servicio de alarma
        AlarmService.startAlarm(context, taskId, title, message)
        
        Log.d(TAG, "Servicio de alarma iniciado para tarea: $taskId")
    }
}