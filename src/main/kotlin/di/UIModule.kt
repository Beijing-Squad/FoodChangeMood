package di

import org.beijing.logic.IngredientGameUseCase
import org.beijing.logic.MealRepository
import org.beijing.presentation.FoodConsoleUi
import org.beijing.presentation.MealUseCases
import org.koin.dsl.module

val uiModule = module {
    single { MealUseCases() }
    single {
        IngredientGameUseCase(
            mealRepository = get()
        )
    }
    single { FoodConsoleUi(get(), get()) }
}
