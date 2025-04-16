package di

import org.beijing.presentation.FoodConsoleUi
import org.beijing.presentation.FoodUseCases
import org.koin.dsl.module

val uiModule = module {
    single { FoodUseCases() }
    single { FoodConsoleUi(get()) }
}