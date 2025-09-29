package cl.jlopezr.multiplatform.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cl.jlopezr.multiplatform.MainActivity

class DismissAndViewTaskReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "DismissAndViewTaskReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "========== DismissAndViewTaskReceiver.onReceive INICIADO ==========")
        Log.d(TAG, "Intent action: ${intent.action}")
        
        if (intent.action == "cl.jlopezr.multiplatform.DISMISS_AND_VIEW_TASK") {
            val taskId = intent.getStringExtra(AlarmService.EXTRA_TASK_ID)
            Log.d(TAG, "Task ID recibido: $taskId")
            
            if (taskId != null) {
                // Detener la alarma primero
                AlarmService.stopAlarm(context)
                Log.d(TAG, "Alarma detenida")
                
                // Abrir MainActivity con el task_id para navegar a la tarea específica
                val mainIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("from_alarm", true)
                    putExtra("view_task_id", taskId)
                }
                
                Log.d(TAG, "Iniciando MainActivity con task_id: $taskId")
                context.startActivity(mainIntent)
                
                // Programar notificación de acción después de un breve delay
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    showTaskActionNotification(context, taskId)
                }, 2000) // 2 segundos de delay
                
            } else {
                Log.e(TAG, "Task ID es null, no se puede proceder")
            }
        }
        
        Log.d(TAG, "========== DismissAndViewTaskReceiver.onReceive COMPLETADO ==========")
    }
    
    private fun showTaskActionNotification(context: Context, taskId: String) {
        Log.d(TAG, "Mostrando notificación simplificada para tarea: $taskId")
        
        // Usar la notificación simplificada que abre la app directamente
        TaskActionNotificationManager.showTaskActionNotification(context, taskId)
    }
}