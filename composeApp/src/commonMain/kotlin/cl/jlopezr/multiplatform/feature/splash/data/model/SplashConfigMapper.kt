package cl.jlopezr.multiplatform.feature.splash.data.model

import cl.jlopezr.multiplatform.feature.splash.domain.model.SplashConfig

/**
 * Mapper para convertir entre SplashConfigDto (capa de datos) y SplashConfig (capa de dominio)
 * Implementa el principio de separación de responsabilidades entre capas
 */
object SplashConfigMapper {
    
    /**
     * Convierte SplashConfigDto a SplashConfig (modelo de dominio)
     */
    fun SplashConfigDto.toDomain(): SplashConfig {
        return SplashConfig(
            appName = this.appName,
            minVersion = this.minVersion,
            theme = this.theme,
            maintenanceMode = this.maintenanceMode,
            welcomeMessage = this.welcomeMessage,
            serverTime = this.serverTime
        )
    }
    
    /**
     * Convierte SplashConfig a SplashConfigDto (para casos especiales)
     */
    fun SplashConfig.toDto(): SplashConfigDto {
        return SplashConfigDto(
            appName = this.appName,
            minVersion = this.minVersion,
            theme = this.theme,
            maintenanceMode = this.maintenanceMode,
            welcomeMessage = this.welcomeMessage,
            serverTime = this.serverTime
        )
    }
}