package cl.jlopezr.multiplatform.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cl.jlopezr.multiplatform.MainActivity

class TaskActionReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "TaskActionReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "========== TaskActionReceiver.onReceive INICIADO ==========")
        Log.d(TAG, "Intent action: ${intent.action}")
        
        val taskId = intent.getStringExtra("task_id")
        Log.d(TAG, "Task ID recibido: $taskId")
        
        if (taskId == null) {
            Log.e(TAG, "Task ID es null, no se puede proceder")
            return
        }
        
        // Cancelar la notificación de acción
        TaskActionNotificationManager.cancelTaskActionNotification(context)
        
        when (intent.action) {
            "cl.jlopezr.multiplatform.COMPLETE_TASK_ACTION" -> {
                Log.d(TAG, "Acción: Completar tarea $taskId")
                handleCompleteTask(context, taskId)
            }
            "cl.jlopezr.multiplatform.UPDATE_TASK_ACTION" -> {
                Log.d(TAG, "Acción: Actualizar tarea $taskId")
                handleUpdateTask(context, taskId)
            }
            else -> {
                Log.w(TAG, "Acción desconocida: ${intent.action}")
            }
        }
        
        Log.d(TAG, "========== TaskActionReceiver.onReceive COMPLETADO ==========")
    }
    
    private fun handleCompleteTask(context: Context, taskId: String) {
        Log.d(TAG, "Manejando completar tarea: $taskId")
        
        // Enviar broadcast para completar la tarea (reutilizando el sistema existente)
        val completeIntent = Intent("cl.jlopezr.multiplatform.COMPLETE_TASK").apply {
            putExtra("task_id", taskId)
            setPackage(context.packageName)
        }
        context.sendBroadcast(completeIntent)
        
        Log.d(TAG, "Broadcast para completar tarea enviado")
    }
    
    private fun handleUpdateTask(context: Context, taskId: String) {
        Log.d(TAG, "Manejando actualizar tarea: $taskId")
        
        // Abrir MainActivity con parámetros para ir directamente al formulario de edición
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("from_alarm", true)
            putExtra("edit_task_id", taskId)
        }
        
        Log.d(TAG, "Iniciando MainActivity para editar tarea: $taskId")
        context.startActivity(mainIntent)
    }
}