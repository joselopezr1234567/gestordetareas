package cl.jlopezr.multiplatform.core.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.core.app.NotificationCompat
import cl.jlopezr.multiplatform.MainActivity

class AlarmService : Service() {
    
    companion object {
        private const val TAG = "AlarmService"
        private const val CHANNEL_ID = "alarm_channel"
        private const val CHANNEL_NAME = "Alarmas de Recordatorios"
        private const val NOTIFICATION_ID = 1001
        
        const val ACTION_START_ALARM = "START_ALARM"
        const val ACTION_STOP_ALARM = "STOP_ALARM"
        const val ACTION_DISMISS_AND_VIEW_TASK = "DISMISS_AND_VIEW_TASK"
        const val EXTRA_TASK_ID = "task_id"
        const val EXTRA_TASK_TITLE = "task_title"
        const val EXTRA_TASK_MESSAGE = "task_message"
        
        fun startAlarm(context: Context, taskId: String, title: String, message: String) {
            val intent = Intent(context, AlarmService::class.java).apply {
                action = ACTION_START_ALARM
                putExtra(EXTRA_TASK_ID, taskId)
                putExtra(EXTRA_TASK_TITLE, title)
                putExtra(EXTRA_TASK_MESSAGE, message)
            }
            context.startForegroundService(intent)
        }
        
        fun stopAlarm(context: Context) {
            val intent = Intent(context, AlarmService::class.java).apply {
                action = ACTION_STOP_ALARM
            }
            context.startService(intent)
        }
    }
    
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private var isAlarmActive = false
    private var currentTaskId: String = ""
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "AlarmService creado")
        createNotificationChannel()
        setupVibrator()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ${intent?.action}")
        
        when (intent?.action) {
            ACTION_START_ALARM -> {
                val taskId = intent.getStringExtra(EXTRA_TASK_ID) ?: ""
                val title = intent.getStringExtra(EXTRA_TASK_TITLE) ?: "Recordatorio de Tarea"
                val message = intent.getStringExtra(EXTRA_TASK_MESSAGE) ?: "Tienes una tarea pendiente"
                
                startAlarm(taskId, title, message)
            }
            ACTION_STOP_ALARM -> {
                stopAlarmAndService()
            }
        }
        
        return START_NOT_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun startAlarm(taskId: String, title: String, message: String) {
        if (isAlarmActive) {
            Log.d(TAG, "Alarma ya está activa")
            return
        }
        
        Log.d(TAG, "Iniciando alarma para tarea: $taskId")
        isAlarmActive = true
        currentTaskId = taskId
        
        // Crear notificación persistente
        val notification = createAlarmNotification(title, message)
        startForeground(NOTIFICATION_ID, notification)
        
        // Iniciar sonido
        startAlarmSound()
        
        // Iniciar vibración
        startVibration()
    }
    
    private fun stopAlarmAndService() {
        Log.d(TAG, "Deteniendo alarma y servicio")
        isAlarmActive = false
        
        // Detener sonido
        stopAlarmSound()
        
        // Detener vibración
        stopVibration()
        
        // Detener servicio
        stopForeground(true)
        stopSelf()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para alarmas de recordatorios de tareas"
                enableVibration(false) // Manejamos la vibración manualmente
                setSound(null, null) // Manejamos el sonido manualmente
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createAlarmNotification(title: String, message: String): Notification {
        // Intent para abrir la aplicación
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Intent para detener la alarma
        val stopAlarmIntent = Intent(this, AlarmService::class.java).apply {
            action = ACTION_STOP_ALARM
        }
        val stopAlarmPendingIntent = PendingIntent.getService(
            this,
            1,
            stopAlarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Intent para apagar y ver tarea
        val dismissAndViewTaskIntent = Intent("cl.jlopezr.multiplatform.DISMISS_AND_VIEW_TASK").apply {
            putExtra(EXTRA_TASK_ID, currentTaskId)
            setPackage(packageName)
        }
        val dismissAndViewTaskPendingIntent = PendingIntent.getBroadcast(
            this,
            2,
            dismissAndViewTaskIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("🔔 $title")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentIntent(openAppPendingIntent)
            .addAction(
                android.R.drawable.ic_media_pause,
                "Apagar Alarma",
                stopAlarmPendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_view,
                "Ver Tarea",
                dismissAndViewTaskPendingIntent
            )
            .setFullScreenIntent(openAppPendingIntent, true)
            .build()
    }
    
    private fun startAlarmSound() {
        try {
            // Obtener el tono de alarma predeterminado
            val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            
            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@AlarmService, alarmUri)
                
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                
                isLooping = true
                
                // Configurar volumen al máximo para alarmas
                val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0)
                
                setOnPreparedListener { player ->
                    Log.d(TAG, "MediaPlayer preparado, iniciando reproducción")
                    player.start()
                }
                
                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "Error en MediaPlayer: what=$what, extra=$extra")
                    false
                }
                
                prepareAsync()
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error al iniciar sonido de alarma", e)
        }
    }
    
    private fun stopAlarmSound() {
        mediaPlayer?.let { player ->
            try {
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
                Log.d(TAG, "Sonido de alarma detenido")
            } catch (e: Exception) {
                Log.e(TAG, "Error al detener sonido", e)
            }
        }
        mediaPlayer = null
    }
    
    private fun setupVibrator() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
    
    private fun startVibration() {
        vibrator?.let { vib ->
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Patrón de vibración: vibrar 1s, pausa 0.5s, repetir
                    val pattern = longArrayOf(0, 1000, 500)
                    val vibrationEffect = VibrationEffect.createWaveform(pattern, 0)
                    vib.vibrate(vibrationEffect)
                } else {
                    @Suppress("DEPRECATION")
                    val pattern = longArrayOf(0, 1000, 500)
                    vib.vibrate(pattern, 0)
                }
                Log.d(TAG, "Vibración iniciada")
            } catch (e: Exception) {
                Log.e(TAG, "Error al iniciar vibración", e)
            }
        }
    }
    
    private fun stopVibration() {
        vibrator?.cancel()
        Log.d(TAG, "Vibración detenida")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "AlarmService destruido")
        stopAlarmSound()
        stopVibration()
        isAlarmActive = false
    }
}