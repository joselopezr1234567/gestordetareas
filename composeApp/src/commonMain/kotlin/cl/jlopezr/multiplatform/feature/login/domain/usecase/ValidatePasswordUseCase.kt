package cl.jlopezr.multiplatform.feature.login.domain.usecase

/**
 * Caso de uso para validar la contraseña
 * Requisitos: mínimo 8 caracteres, al menos una mayúscula, una minúscula, un número y un carácter especial
 */
class ValidatePasswordUseCase {
    
    /**
     * Valida la contraseña según los requisitos establecidos
     * @param password Contraseña a validar
     * @return Resultado de la validación
     */
    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña no puede estar vacía"
            )
        }
        
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe tener al menos 8 caracteres"
            )
        }
        
        val hasUpperCase = password.any { it.isUpperCase() }
        if (!hasUpperCase) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe contener al menos una letra mayúscula"
            )
        }
        
        val hasLowerCase = password.any { it.isLowerCase() }
        if (!hasLowerCase) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe contener al menos una letra minúscula"
            )
        }
        
        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe contener al menos un número"
            )
        }
        
        val specialCharacters = "!@#$%^&*()_+-=[]{}|;:,.<>?"
        val hasSpecialChar = password.any { it in specialCharacters }
        if (!hasSpecialChar) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe contener al menos un carácter especial (!@#$%^&*()_+-=[]{}|;:,.<>?)"
            )
        }
        
        return ValidationResult(successful = true)
    }
}