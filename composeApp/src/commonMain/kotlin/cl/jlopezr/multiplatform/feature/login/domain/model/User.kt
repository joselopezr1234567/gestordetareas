package cl.jlopezr.multiplatform.feature.login.domain.model

/**
 * Modelo de dominio para el usuario
 */
data class User(
    val id: String,
    val email: String,
    val name: String,
    val profilePicture: String? = null
) {
    fun getDisplayName(): String {
        return name.ifEmpty { email.substringBefore("@") }
    }
    
    fun hasProfilePicture(): Boolean {
        return !profilePicture.isNullOrEmpty()
    }
}