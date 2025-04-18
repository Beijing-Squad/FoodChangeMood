package di

import org.beijing.logic.usecases.*
import org.koin.dsl.module

val logicModule = module {
    single { ViewMealsUseCases(get()) }
    single { GamesMealsUseCases(get()) }
    single { SearchMealsUseCases(get()) }
    single { SuggestionMealsUseCases(get()) }
    single { ExploreCountryMealsUseCase(get()) }
}