package cl.jlopezr.multiplatform.di

import cl.jlopezr.multiplatform.feature.home.presentation.viewmodel.TaskFormViewModel
import cl.jlopezr.multiplatform.feature.home.presentation.viewmodel.TaskListViewModel
import cl.jlopezr.multiplatform.feature.splash.presentation.SplashViewModel
import cl.jlopezr.multiplatform.feature.login.presentation.LoginViewModel
import cl.jlopezr.multiplatform.feature.settings.presentation.viewmodel.SettingsViewModel
import cl.jlopezr.multiplatform.feature.drawer.presentation.AppDrawerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Módulo de ViewModels
 * Configura las dependencias de los ViewModels usando Koin Compose
 */
val viewModelModule = module {
    
    // Task ViewModels
    viewModel { 
        TaskListViewModel(taskUseCases = get()) 
    }
    
    viewModel { 
        TaskFormViewModel(taskUseCases = get()) 
    }
    
    // Splash ViewModel
    viewModel { 
        SplashViewModel(
            loadInitialDataUseCase = get(),
            checkAppVersionUseCase = get(),
            validateUserSessionUseCase = get()
        ) 
    }
    
    // Login ViewModel
    viewModel { 
        LoginViewModel(
            loginUseCase = get(),
            validateEmailUseCase = get(),
            validatePasswordUseCase = get()
        ) 
    }
    
    // Settings ViewModel
    viewModel { 
        SettingsViewModel(
            themeManager = get(),
            taskUseCases = get()
        ) 
    }
    
    // Drawer ViewModel
    viewModel { 
        AppDrawerViewModel(
            logoutUseCase = get()
        ) 
    }
}