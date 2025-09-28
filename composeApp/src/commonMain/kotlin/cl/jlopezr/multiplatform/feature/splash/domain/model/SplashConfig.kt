package cl.jlopezr.multiplatform.feature.splash.domain.model

/**
 * Modelo de dominio para la configuración del splash
 * Representa la entidad de negocio pura, independiente de la capa de datos
 */
data class SplashConfig(
    val appName: String,
    val minVersion: String,
    val theme: String,
    val maintenanceMode: Boolean,
    val welcomeMessage: String,
    val serverTime: Long
) {
    /**
     * Verifica si la aplicación está en modo mantenimiento
     */
    fun isInMaintenanceMode(): Boolean = maintenanceMode
    
    /**
     * Obtiene el mensaje de bienvenida formateado
     */
    fun getFormattedWelcomeMessage(): String {
        return if (welcomeMessage.isNotBlank()) {
            welcomeMessage
        } else {
            "Bienvenido a $appName"
        }
    }
}