package cl.jlopezr.multiplatform.feature.splash.domain.usecase

import cl.jlopezr.multiplatform.core.util.Resource
import cl.jlopezr.multiplatform.feature.splash.domain.model.SplashConfig
import cl.jlopezr.multiplatform.feature.splash.domain.repository.SplashRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para cargar los datos iniciales de la aplicación
 * Obtiene la configuración del splash y datos necesarios para el inicio
 * Parte de la lógica de negocio del feature Splash
 */
class LoadInitialDataUseCase(
    private val repository: SplashRepository
) {
    
    /**
     * Ejecuta la carga de datos iniciales
     * @return Flow<Resource<SplashConfig>> - configuración del splash
     */
    operator fun invoke(): Flow<Resource<SplashConfig>> {
        return repository.getSplashConfig()
    }
}