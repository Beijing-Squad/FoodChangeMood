package di

import org.beijing.presentation.FoodConsoleUi
import org.beijing.presentation.MealUseCases
import org.koin.dsl.module

val uiModule = module {
    single { MealUseCases(get(),) }
    single { FoodConsoleUi(get()) }
}