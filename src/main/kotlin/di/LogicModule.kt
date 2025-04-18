package di

import org.beijing.logic.usecases.*
import org.koin.dsl.module

val logicModule = module {
    single { ManageMealsViewsUseCases(get()) }
    single { ManageMealsGamesUseCases(get()) }
    single { ManageMealsSearchUseCases(get()) }
    single { ManageMealsSuggestionsUseCases(get()) }
}