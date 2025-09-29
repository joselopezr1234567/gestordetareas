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
        Log.i(TAG, "========== AlarmReceiver.onReceive INICIADO ==========")
        Log.i(TAG, "Intent action: ${intent.action}")
        Log.i(TAG, "Intent extras: ${intent.extras}")
        Log.i(TAG, "Context: ${context.javaClass.simpleName}")
        
        val taskId = intent.getStringExtra(EXTRA_TASK_ID)
        val title = intent.getStringExtra(EXTRA_TASK_TITLE) ?: "Recordatorio de Tarea"
        val message = intent.getStringExtra(EXTRA_TASK_MESSAGE) ?: "Tienes una tarea pendiente"
        
        Log.i(TAG, "Datos extraídos - ID: $taskId, Título: $title, Mensaje: $message")
        
        if (taskId == null) {
            Log.e(TAG, "task_id es null, no se puede iniciar la alarma")
            return
        }
        
        Log.i(TAG, "Iniciando servicio de alarma...")
        // Iniciar el servicio de alarma PRIMERO
        AlarmService.startAlarm(context, taskId, title, message)
        
        // Esperar un poco antes de completar la tarea para que el usuario escuche la alarma
        Log.i(TAG, "Programando completado automático de tarea en 10 segundos...")
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            sendTaskCompletionBroadcast(context, taskId)
        }, 10000) // 10 segundos de delay
        
        Log.i(TAG, "========== AlarmReceiver.onReceive COMPLETADO ==========")
    }
    
    private fun sendTaskCompletionBroadcast(context: Context, taskId: String) {
        try {
            Log.i(TAG, "Enviando broadcast para completar tarea: $taskId")
            val intent = Intent("cl.jlopezr.multiplatform.COMPLETE_TASK").apply {
                putExtra("task_id", taskId)
                setPackage(context.packageName) // Asegurar que solo nuestra app reciba el broadcast
            }
            context.sendBroadcast(intent)
            Log.i(TAG, "Broadcast enviado exitosamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error al enviar broadcast: ${e.message}")
        }
    }
}