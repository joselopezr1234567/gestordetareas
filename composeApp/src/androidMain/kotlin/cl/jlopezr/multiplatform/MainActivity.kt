package cl.jlopezr.multiplatform

import android.Manifest
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
import cl.jlopezr.multiplatform.core.storage.DataStoreFactory
import cl.jlopezr.multiplatform.core.notification.NotificationManagerFactory

class MainActivity : ComponentActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
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

        setContent {
            App()
        }
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