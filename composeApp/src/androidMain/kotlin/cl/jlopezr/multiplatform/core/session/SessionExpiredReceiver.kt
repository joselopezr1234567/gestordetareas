package cl.jlopezr.multiplatform.core.session

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cl.jlopezr.multiplatform.MainActivity

/**
 * BroadcastReceiver que maneja la expiración de sesión
 * Redirige al usuario al login cuando la sesión expira
 */
class SessionExpiredReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "SessionExpiredReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Broadcast recibido: ${intent.action}")
        
        when (intent.action) {
            MainActivity.ACTION_SESSION_EXPIRED -> {
                Log.w(TAG, "Sesión expirada detectada, redirigiendo al login")
                redirectToLogin(context)
            }
        }
    }
    
    /**
     * Redirige al usuario a la pantalla de login
     */
    private fun redirectToLogin(context: Context) {
        Log.d(TAG, "Iniciando MainActivity con flag para mostrar login")
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("force_login", true)
        }
        
        context.startActivity(intent)
    }
}