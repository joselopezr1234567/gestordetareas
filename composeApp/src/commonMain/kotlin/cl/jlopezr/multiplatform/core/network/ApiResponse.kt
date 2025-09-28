package cl.jlopezr.multiplatform.core.network

/**
 * Clase sellada que representa las respuestas de la API
 * Utilizada por FakeApiRepository para simular respuestas de red
 */
sealed class ApiResponse<T> {
    /**
     * Respuesta exitosa con datos
     */
    data class Success<T>(val data: T) : ApiResponse<T>()
    
    /**
     * Respuesta de error con mensaje y código opcional
     */
    data class Error<T>(
        val message: String,
        val code: Int? = null,
        val exception: Throwable? = null
    ) : ApiResponse<T>()
    
    /**
     * Estado de carga
     */
    data class Loading<T>(val isLoading: Boolean = true) : ApiResponse<T>()
}