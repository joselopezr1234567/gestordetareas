package cl.jlopezr.multiplatform.di

import org.koin.dsl.module

/**
 * Módulo principal de la aplicación
 * Combina todos los módulos de dependencias
 */
val appModule = module {
    includes(
        networkModule,
        repositoryModule,
        useCaseModule,
        viewModelModule,
        themeModule
    )
}