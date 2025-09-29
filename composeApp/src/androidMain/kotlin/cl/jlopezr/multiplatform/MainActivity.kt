package cl.jlopezr.multiplatform

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import cl.jlopezr.multiplatform.core.storage.DataStoreFactory
import cl.jlopezr.multiplatform.core.notification.NotificationManagerFactory
import cl.jlopezr.multiplatform.feature.login.domain.repository.LoginRepository
import cl.jlopezr.multiplatform.core.util.Resource
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
        const val ACTION_SESSION_EXPIRED = "cl.jlopezr.multiplatform.SESSION_EXPIRED"
        private const val PREFS_NAME = "alarm_prefs"
        private const val KEY_LAST_ACTION_FROM_ALARM = "last_action_from_alarm"
        private const val ALARM_ACTION_TIMEOUT = 30000L // 30 segundos
    }
    
    private val loginRepository: LoginRepository by inject()
    private var isAppInBackground = false
    private var isFromAlarm = false
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG, "Permiso de notificación concedido")
        } else {
            Log.w(TAG, "Permiso de notificación denegado")
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Inicializar DataStoreFactory con el contexto de la aplicación
        DataStoreFactory.initialize(applicationContext)
        
        // Inicializar NotificationManagerFactory con el contexto de la aplicación
        NotificationManagerFactory.initialize(applicationContext)
        
        // Solicitar permisos de notificación para Android 13+
        requestNotificationPermission()

        // Verificar si se debe forzar el login
        val forceLogin = intent.getBooleanExtra("force_login", false)
        isFromAlarm = intent.getBooleanExtra("from_alarm", false)
        val fromNotification = intent.getBooleanExtra("from_notification", false)
        val viewTaskId = intent.getStringExtra("view_task_id")
        val editTaskId = intent.getStringExtra("edit_task_id")
        val taskActionId = intent.getStringExtra("task_action_id")
        val viewTaskFromNotification = intent.getStringExtra("view_task_from_notification")
        Log.d(TAG, "onCreate - forceLogin: $forceLogin, fromAlarm: $isFromAlarm, fromNotification: $fromNotification, viewTaskId: $viewTaskId, editTaskId: $editTaskId, taskActionId: $taskActionId, viewTaskFromNotification: $viewTaskFromNotification")
        
        // Si viene desde una alarma, marcar la acción
        if (isFromAlarm) {
            markLastActionFromAlarm()
        }

        setContent {
            App(
                forceLogin = forceLogin,
                viewTaskId = viewTaskId,
                editTaskId = editTaskId,
                taskActionId = taskActionId,
                viewTaskFromNotification = viewTaskFromNotification
            )
        }
    }
    
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume - isAppInBackground: $isAppInBackground, isFromAlarm: $isFromAlarm")
        
        // Verificar si la última acción fue desde una alarma
        val wasRecentAlarmAction = wasLastActionFromAlarm()
        
        // Si la app estaba en background y NO viene desde la alarma y NO hubo acción reciente desde alarma, verificar la sesión
        if (isAppInBackground && !isFromAlarm && !wasRecentAlarmAction) {
            verifySessionOnResume()
        } else if (wasRecentAlarmAction) {
            // Si hubo una acción reciente desde alarma, limpiar la marca después de un tiempo
            clearLastActionFromAlarm()
        }
        
        isAppInBackground = false
        isFromAlarm = false // Reset después de usar
    }
    
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause - marcando app como en background")
        isAppInBackground = true
    }
    
    /**
     * Verifica la sesión cuando la aplicación vuelve del background
     */
    private fun verifySessionOnResume() {
        Log.d(TAG, "Verificando sesión después de volver del background")
        
        lifecycleScope.launch {
            try {
                loginRepository.isUserLoggedIn().collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val isLoggedIn = resource.data ?: false
                            Log.d(TAG, "Estado de sesión: $isLoggedIn")
                            
                            if (!isLoggedIn) {
                                Log.w(TAG, "Sesión expirada, enviando broadcast para redirigir al login")
                                sendSessionExpiredBroadcast()
                            }
                        }
                        is Resource.Error -> {
                            Log.e(TAG, "Error al verificar sesión: ${resource.message}")
                            // En caso de error, asumir sesión inválida
                            sendSessionExpiredBroadcast()
                        }
                        is Resource.Loading -> {
                            Log.d(TAG, "Verificando sesión...")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al verificar sesión: ${e.message}")
                sendSessionExpiredBroadcast()
            }
        }
    }
    
    /**
     * Envía un broadcast para notificar que la sesión ha expirado
     */
    private fun sendSessionExpiredBroadcast() {
        val intent = Intent(ACTION_SESSION_EXPIRED)
        sendBroadcast(intent)
        Log.d(TAG, "Broadcast de sesión expirada enviado")
    }
    
    /**
     * Marca que la última acción fue desde una alarma
     */
    private fun markLastActionFromAlarm() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putLong(KEY_LAST_ACTION_FROM_ALARM, System.currentTimeMillis())
            .apply()
        Log.d(TAG, "Marcada última acción desde alarma")
    }
    
    /**
     * Verifica si la última acción fue desde una alarma (dentro del timeout)
     */
    private fun wasLastActionFromAlarm(): Boolean {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastActionTime = prefs.getLong(KEY_LAST_ACTION_FROM_ALARM, 0L)
        val currentTime = System.currentTimeMillis()
        val wasFromAlarm = (currentTime - lastActionTime) < ALARM_ACTION_TIMEOUT
        Log.d(TAG, "¿Última acción desde alarma? $wasFromAlarm (hace ${currentTime - lastActionTime}ms)")
        return wasFromAlarm
    }
    
    /**
     * Limpia la marca de acción desde alarma
     */
    private fun clearLastActionFromAlarm() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .remove(KEY_LAST_ACTION_FROM_ALARM)
            .apply()
        Log.d(TAG, "Limpiada marca de acción desde alarma")
    }
    
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d(TAG, "Permiso de notificación ya concedido")
                }
                else -> {
                    Log.d(TAG, "Solicitando permiso de notificación")
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            Log.d(TAG, "Android < 13, no se requiere permiso de notificación")
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}