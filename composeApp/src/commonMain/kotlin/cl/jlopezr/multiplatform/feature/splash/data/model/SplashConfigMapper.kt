package cl.jlopezr.multiplatform.feature.splash.data.model

import cl.jlopezr.multiplatform.feature.splash.domain.model.SplashConfig


object SplashConfigMapper {
    

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