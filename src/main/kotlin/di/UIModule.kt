package di

import org.beijing.presentation.FoodConsoleUi
import org.beijing.presentation.FoodUi
import org.beijing.presentation.service.SearchMealService
import org.beijing.presentation.service.ViewMealsService
import org.koin.dsl.module

val uiModule = module {
    single { SearchMealService() }
    single { ViewMealsService() }
    single<FoodUi> {
        FoodConsoleUi(
            searchMealService = get(),
            viewMealsService = get(),
        )
    }
}