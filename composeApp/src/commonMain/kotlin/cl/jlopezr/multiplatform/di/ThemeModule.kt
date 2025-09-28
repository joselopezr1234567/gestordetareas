package cl.jlopezr.multiplatform.di

import cl.jlopezr.multiplatform.core.theme.ThemeManager
import org.koin.dsl.module

/**
 * Módulo de Koin para el sistema de temas
 */
val themeModule = module {
    single { ThemeManager() }
}