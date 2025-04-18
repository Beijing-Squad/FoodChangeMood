package di

import org.beijing.logic.usecases.GamesMealsUseCases
import org.beijing.logic.usecases.SearchMealsUseCases
import org.beijing.logic.usecases.SuggestionMealsUseCases
import org.beijing.logic.usecases.ViewMealsUseCases
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.EasyFoodSuggestionUseCase
import org.beijing.logic.usecases.ExploreCountryMealsUseCase
import org.beijing.logic.usecases.MealUseCases
import org.koin.dsl.module

val logicModule = module {
    single { ViewMealsUseCases(get()) }
    single { GamesMealsUseCases(get()) }
    single { SearchMealsUseCases(get()) }
    single { SuggestionMealsUseCases(get()) }
    single { ExploreCountryMealsUseCase(get<MealRepository>()) }
    single { EasyFoodSuggestionUseCase(get<MealRepository>()) }

}