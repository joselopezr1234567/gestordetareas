package cl.jlopezr.multiplatform.feature.login.domain.usecase


class ValidateEmailUseCase {
    

    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El email no puede estar vacío"
            )
        }
        
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!email.matches(emailRegex)) {
            return ValidationResult(
                successful = false,
                errorMessage = "Formato de email inválido"
            )
        }
        
        return ValidationResult(successful = true)
    }
}


data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)