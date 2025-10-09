package cl.jlopezr.multiplatform.feature.splash.domain.model


data class SplashConfig(
    val appName: String,
    val minVersion: String,
    val theme: String,
    val maintenanceMode: Boolean,
    val welcomeMessage: String,
    val serverTime: Long
) {

    fun isInMaintenanceMode(): Boolean = maintenanceMode
    

    fun getFormattedWelcomeMessage(): String {
        return if (welcomeMessage.isNotBlank()) {
            welcomeMessage
        } else {
            "Bienvenido a $appName"
        }
    }
}