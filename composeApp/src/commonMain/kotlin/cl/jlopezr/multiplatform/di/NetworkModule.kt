package cl.jlopezr.multiplatform.di

import cl.jlopezr.multiplatform.core.network.FakeApiRepository
import cl.jlopezr.multiplatform.core.storage.PreferencesManager
import cl.jlopezr.multiplatform.feature.splash.data.datasource.local.SplashLocalDataSource
import cl.jlopezr.multiplatform.feature.splash.data.datasource.remote.SplashRemoteDataSource
import cl.jlopezr.multiplatform.feature.login.data.datasource.local.LoginLocalDataSource
import cl.jlopezr.multiplatform.feature.login.data.datasource.remote.LoginRemoteDataSource
import org.koin.dsl.module

/**
 * Módulo de red y data sources
 * Configura las dependencias relacionadas con la red y almacenamiento
 */
val networkModule = module {
    
    // Core Network & Storage
    single { FakeApiRepository() }
    single { PreferencesManager(dataStore = get()) }
    
    // Splash Data Sources
    single { SplashLocalDataSource() }
    single { SplashRemoteDataSource(get()) }
    
    // Login Data Sources
    single { LoginLocalDataSource(get()) }
    single { LoginRemoteDataSource(get()) }
}