package cl.jlopezr.multiplatform.feature.splash.data.datasource.remote

import cl.jlopezr.multiplatform.core.network.ApiResponse
import cl.jlopezr.multiplatform.core.network.FakeApiRepository
import cl.jlopezr.multiplatform.feature.splash.data.model.SplashConfigDto
import cl.jlopezr.multiplatform.feature.splash.data.model.VersionCheckDto


class SplashRemoteDataSource(
    private val fakeApiRepository: FakeApiRepository
) {
    

    suspend fun getSplashConfig(): ApiResponse<SplashConfigDto> {
        return fakeApiRepository.getSplashConfig()
    }
    

    suspend fun validateUserSession(token: String?): ApiResponse<Boolean> {
        return fakeApiRepository.validateUserSession(token)
    }
    

    suspend fun checkAppVersion(currentVersion: String): ApiResponse<VersionCheckDto> {
        return fakeApiRepository.checkAppVersion(currentVersion)
    }
}