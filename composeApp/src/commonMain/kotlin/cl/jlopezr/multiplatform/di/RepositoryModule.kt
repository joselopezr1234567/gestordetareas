package cl.jlopezr.multiplatform.di

import cl.jlopezr.multiplatform.core.storage.DataStoreFactory
import cl.jlopezr.multiplatform.feature.home.data.datasource.TaskLocalDataSource
import cl.jlopezr.multiplatform.feature.home.data.repository.TaskRepositoryImpl
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import cl.jlopezr.multiplatform.feature.splash.data.repository.SplashRepositoryImpl
import cl.jlopezr.multiplatform.feature.splash.domain.repository.SplashRepository
import cl.jlopezr.multiplatform.feature.login.data.repository.LoginRepositoryImpl
import cl.jlopezr.multiplatform.feature.login.domain.repository.LoginRepository
import org.koin.dsl.module

/**
 * Módulo de repositorios
 * Configura las implementaciones de los repositorios del dominio
 */
val repositoryModule = module {
    
    // DataStore
    single { DataStoreFactory.createDataStore() }
    
    // Task Data Source
    single { TaskLocalDataSource(dataStore = get()) }
    
    // Task Repository
    single<TaskRepository> { 
        TaskRepositoryImpl(localDataSource = get()) 
    }
    
    // Splash Repository
    single<SplashRepository> { 
        SplashRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get()
        ) 
    }
    
    // Login Repository
    single<LoginRepository> { 
        LoginRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get()
        ) 
    }
}