package cl.jlopezr.multiplatform.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cl.jlopezr.multiplatform.feature.home.domain.usecase.ToggleTaskCompletionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

class TaskCompletionReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "TaskCompletionReceiver"
        private const val PREFS_NAME = "alarm_prefs"
        private const val KEY_LAST_ACTION_FROM_ALARM = "last_action_from_alarm"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "========== TaskCompletionReceiver.onReceive INICIADO ==========")
        
        if (intent.action == "cl.jlopezr.multiplatform.COMPLETE_TASK") {
            val taskId = intent.getStringExtra("task_id")
            
            if (taskId != null) {
                Log.i(TAG, "Completando automáticamente tarea: $taskId")
                // Marcar que esta acción viene desde una alarma
                markLastActionFromAlarm(context)
                completeTaskAutomatically(taskId)
            } else {
                Log.e(TAG, "task_id es null en el broadcast")
            }
        }
        
        Log.i(TAG, "========== TaskCompletionReceiver.onReceive COMPLETADO ==========")
    }
    
    private fun completeTaskAutomatically(taskId: String) {
        try {
            // Obtener el use case desde Koin (que ya está inicializado en la app principal)
            val toggleTaskCompletionUseCase = GlobalContext.get().get<ToggleTaskCompletionUseCase>()
            
            // Ejecutar en una corrutina
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = toggleTaskCompletionUseCase(taskId)
                    if (result.isSuccess) {
                        Log.i(TAG, "Tarea $taskId completada automáticamente")
                    } else {
                        Log.e(TAG, "Error al completar tarea $taskId: ${result.exceptionOrNull()?.message}")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Excepción al completar tarea $taskId: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al acceder al use case: ${e.message}")
        }
    }
    
    /**
     * Marca que la última acción fue desde una alarma
     */
    private fun markLastActionFromAlarm(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putLong(KEY_LAST_ACTION_FROM_ALARM, System.currentTimeMillis())
            .apply()
        Log.i(TAG, "Marcada última acción desde alarma")
    }
}