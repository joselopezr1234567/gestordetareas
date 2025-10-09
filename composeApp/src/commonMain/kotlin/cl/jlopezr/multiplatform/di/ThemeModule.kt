package cl.jlopezr.multiplatform.di

import cl.jlopezr.multiplatform.core.theme.ThemeManager
import org.koin.dsl.module


val themeModule = module {
    single { ThemeManager() }
}