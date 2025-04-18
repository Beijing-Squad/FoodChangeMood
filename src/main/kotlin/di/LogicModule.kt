package di

import org.beijing.logic.usecases.*
import org.koin.dsl.module

val logicModule = module {
    single { ViewMealsUseCases(get()) }
    single { GamesMealsUseCases(get()) }
    single { SuggestionMealsUseCases(get()) }
    single { SearchMealsUseCases(get())}
}