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
import presentation.view_read.ConsoleIO
import presentation.view_read.SystemConsoleIO

val uiModule = module {
    single<ViewMealDetails> {
        ConsoleViewMealDetails()
    }
    single<ConsoleIO> { SystemConsoleIO() }
    single { SearchMealService(get(),get(),get()) }
    single { SuggestionMealsService(get(),get(),get()) }
    single { ViewMealsService(get(),get()) }
    single { GameMealsService(get(),get()) }
    single<FoodUi> {
        FoodConsoleUi(
            searchMealService = get(),
            viewMealsService = get(),
            gameMealsService = get(),
            suggestionMealsService = get(),
            consoleIO = get()
        )
    }

}