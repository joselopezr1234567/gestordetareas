package cl.jlopezr.multiplatform.core.network


sealed class ApiResponse<T> {

    data class Success<T>(val data: T) : ApiResponse<T>()
    

    data class Error<T>(
        val message: String,
        val code: Int? = null,
        val exception: Throwable? = null
    ) : ApiResponse<T>()
    

    data class Loading<T>(val isLoading: Boolean = true) : ApiResponse<T>()
}