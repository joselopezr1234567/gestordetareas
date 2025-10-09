package cl.jlopezr.multiplatform.di

import org.koin.dsl.module


val appModule = module {
    includes(
        networkModule,
        repositoryModule,
        useCaseModule,
        viewModelModule,
        themeModule
    )
}