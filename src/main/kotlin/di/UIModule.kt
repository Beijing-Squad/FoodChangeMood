package di

import org.beijing.presentation.ConsoleViewMealDetails
import org.beijing.presentation.FoodConsoleUi
import org.beijing.presentation.FoodUi
import org.beijing.presentation.ViewMealDetails
import org.beijing.presentation.service.GameMealsService
import org.beijing.presentation.service.SearchMealService
import org.beijing.presentation.service.SuggestionMealsService
import org.beijing.presentation.service.ViewMealsService
import org.koin.dsl.module

val uiModule = module {
    single { SearchMealService() }
    single { SuggestionMealsService() }
    single { ViewMealsService() }
    single { GameMealsService() }
    single<FoodUi> {
        FoodConsoleUi(
            searchMealService = get(),
            viewMealsService = get(),
            gameMealsService = get(),
            suggestionMealsService = get()
        )
    }
    single<ViewMealDetails> {
        ConsoleViewMealDetails()
    }
}