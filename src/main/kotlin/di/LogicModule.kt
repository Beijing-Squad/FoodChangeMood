package di


import org.beijing.logic.usecases.GamesMealsUseCases
import org.beijing.logic.usecases.SearchMealsUseCases
import org.beijing.logic.usecases.SuggestionMealsUseCases
import org.beijing.logic.usecases.ViewMealsUseCase
import org.koin.dsl.module

val logicModule = module {
    single { ViewMealsUseCase(get()) }
    single { GamesMealsUseCases(get()) }
    single { SearchMealsUseCases(get()) }
    single { SuggestionMealsUseCases(get()) }

}