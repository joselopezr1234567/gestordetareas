package cl.jlopezr.multiplatform.core.notification

import android.content.Context

actual object NotificationManagerFactory {
    private lateinit var context: Context
    
    fun initialize(context: Context) {
        this.context = context.applicationContext
    }
    
    actual fun create(): NotificationManager {
        return AndroidNotificationManager(context)
    }
}