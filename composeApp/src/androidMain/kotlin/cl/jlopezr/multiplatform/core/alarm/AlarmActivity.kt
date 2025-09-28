package cl.jlopezr.multiplatform.core.alarm

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.jlopezr.multiplatform.MainActivity
import kotlinx.coroutines.delay

class AlarmActivity : ComponentActivity() {
    
    companion object {
        private const val TAG = "AlarmActivity"
        const val EXTRA_TASK_TITLE = "task_title"
        const val EXTRA_TASK_MESSAGE = "task_message"
        
        fun createIntent(context: Context, title: String, message: String): Intent {
            return Intent(context, AlarmActivity::class.java).apply {
                putExtra(EXTRA_TASK_TITLE, title)
                putExtra(EXTRA_TASK_MESSAGE, message)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or 
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NO_HISTORY
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "AlarmActivity creada")
        
        // Configurar para mostrar sobre la pantalla de bloqueo
        setupWindowFlags()
        
        val title = intent.getStringExtra(EXTRA_TASK_TITLE) ?: "Recordatorio de Tarea"
        val message = intent.getStringExtra(EXTRA_TASK_MESSAGE) ?: "Tienes una tarea pendiente"
        
        setContent {
            AlarmScreen(
                title = title,
                message = message,
                onStopAlarm = {
                    stopAlarm()
                },
                onOpenApp = {
                    openMainApp()
                }
            )
        }
    }
    
    private fun setupWindowFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }
    }
    
    private fun stopAlarm() {
        Log.d(TAG, "Deteniendo alarma desde AlarmActivity")
        AlarmService.stopAlarm(this)
        finish()
    }
    
    private fun openMainApp() {
        Log.d(TAG, "Abriendo aplicación principal")
        AlarmService.stopAlarm(this)
        
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
    
    override fun onBackPressed() {
        // No permitir cerrar con el botón de atrás
        // El usuario debe usar los botones de la interfaz
    }
}

@Composable
fun AlarmScreen(
    title: String,
    message: String,
    onStopAlarm: () -> Unit,
    onOpenApp: () -> Unit
) {
    var isBlinking by remember { mutableStateOf(false) }
    
    // Efecto de parpadeo para el icono de alarma
    LaunchedEffect(Unit) {
        while (true) {
            isBlinking = !isBlinking
            delay(500)
        }
    }
    
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Icono de alarma con efecto de parpadeo
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                if (isBlinking) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.errorContainer
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = "Alarma",
                            modifier = Modifier.size(48.dp),
                            tint = if (isBlinking) Color.White else MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    
                    // Título
                    Text(
                        text = "🔔 ALARMA ACTIVA",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    
                    // Título de la tarea
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    // Mensaje de la tarea
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Botones de acción
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Botón principal: Apagar alarma
                        Button(
                            onClick = onStopAlarm,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "APAGAR ALARMA",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        // Botón secundario: Abrir aplicación
                        OutlinedButton(
                            onClick = onOpenApp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Abrir Aplicación",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}