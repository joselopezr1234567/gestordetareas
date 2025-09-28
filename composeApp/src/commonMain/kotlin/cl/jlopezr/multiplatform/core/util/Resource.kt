package cl.jlopezr.multiplatform.core.util

/**
 * Clase sellada que representa el estado de una operación asíncrona
 * Utilizada en toda la aplicación para manejar estados de carga, éxito y error
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Estado de éxito con datos
     */
    class Success<T>(data: T) : Resource<T>(data)
    
    /**
     * Estado de error con mensaje opcional
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    
    /**
     * Estado de carga
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)
}