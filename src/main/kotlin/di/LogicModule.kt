package di

import org.beijing.logic.usecases.ViewMealsUseCase
import org.koin.dsl.module

val logicModule = module {
    single { ViewMealsUseCase(get()) }
}