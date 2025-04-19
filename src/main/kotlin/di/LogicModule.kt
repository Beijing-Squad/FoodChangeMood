package di

import org.beijing.logic.usecases.*
import org.koin.dsl.module

val logicModule = module {
    single { ManageMealsViewsUseCases(get()) }
    single { ManageMealsGamesUseCase(get()) }
    single { ManageMealsSearchUseCase(get()) }
    single { ManageMealsSuggestionsUseCases(get()) }
}