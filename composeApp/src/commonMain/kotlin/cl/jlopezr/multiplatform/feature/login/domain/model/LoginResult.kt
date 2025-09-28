package cl.jlopezr.multiplatform.feature.login.domain.model

/**
 * Modelo de dominio para el resultado del login (sin tokens)
 */
data class LoginResult(
    val success: Boolean,
    val user: User?,
    val message: String?
) {
    fun isSuccessful(): Boolean = success && user != null
    
    fun getErrorMessage(): String = message ?: "Error desconocido en el login"
}