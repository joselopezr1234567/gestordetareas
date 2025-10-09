package cl.jlopezr.multiplatform.di

import cl.jlopezr.multiplatform.core.storage.DataStoreFactory
import cl.jlopezr.multiplatform.core.storage.PreferencesManager
import cl.jlopezr.multiplatform.feature.home.data.datasource.TaskLocalDataSource
import cl.jlopezr.multiplatform.feature.home.data.repository.TaskRepositoryImpl
import cl.jlopezr.multiplatform.feature.home.domain.repository.TaskRepository
import cl.jlopezr.multiplatform.feature.splash.data.repository.SplashRepositoryImpl
import cl.jlopezr.multiplatform.feature.splash.domain.repository.SplashRepository
import cl.jlopezr.multiplatform.feature.login.data.repository.LoginRepositoryImpl
import cl.jlopezr.multiplatform.feature.login.domain.repository.LoginRepository
import org.koin.dsl.module


val repositoryModule = module {
    

    single { DataStoreFactory.createDataStore() }
    

    single { PreferencesManager(dataStore = get()) }
    

    single { TaskLocalDataSource(dataStore = get()) }
    

    single<TaskRepository> { 
        TaskRepositoryImpl(localDataSource = get()) 
    }
    

    single<SplashRepository> { 
        SplashRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get()
        ) 
    }
    

    single<LoginRepository> { 
        LoginRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get()
        ) 
    }
}