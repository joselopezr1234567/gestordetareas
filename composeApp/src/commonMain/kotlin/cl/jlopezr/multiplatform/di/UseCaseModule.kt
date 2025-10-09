package cl.jlopezr.multiplatform.di

import cl.jlopezr.multiplatform.feature.home.domain.usecase.*
import cl.jlopezr.multiplatform.feature.splash.domain.usecase.CheckAppVersionUseCase
import cl.jlopezr.multiplatform.feature.splash.domain.usecase.LoadInitialDataUseCase
import cl.jlopezr.multiplatform.feature.splash.domain.usecase.ValidateUserSessionUseCase
import cl.jlopezr.multiplatform.feature.login.domain.repository.LoginRepository
import cl.jlopezr.multiplatform.feature.login.domain.usecase.LoginUseCase
import cl.jlopezr.multiplatform.feature.login.domain.usecase.LogoutUseCase
import cl.jlopezr.multiplatform.feature.login.domain.usecase.IsUserLoggedInUseCase
import cl.jlopezr.multiplatform.feature.login.domain.usecase.ValidateEmailUseCase
import cl.jlopezr.multiplatform.feature.login.domain.usecase.ValidatePasswordUseCase
import org.koin.dsl.module


val useCaseModule = module {
    

    factory { GetFilteredTasksUseCase(get()) }
    factory { GetTaskByIdUseCase(get()) }
    factory { CreateTaskUseCase(get()) }
    factory { UpdateTaskUseCase(get()) }
    factory { DeleteTaskUseCase(get()) }
    factory { ToggleTaskCompletionUseCase(get()) }
    factory { GetTaskStatisticsUseCase(get()) }
    

    factory { 
        TaskUseCases(
            getFilteredTasks = get(),
            getTaskById = get(),
            createTask = get(),
            updateTask = get(),
            deleteTask = get(),
            toggleTaskCompletion = get(),
            getTaskStatistics = get()
        )
    }
    

    factory { CheckAppVersionUseCase(get()) }
    factory { LoadInitialDataUseCase(get()) }
    factory { ValidateUserSessionUseCase(get<LoginRepository>()) }
    

    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { IsUserLoggedInUseCase(get()) }
    factory { ValidateEmailUseCase() }
    factory { ValidatePasswordUseCase() }
}