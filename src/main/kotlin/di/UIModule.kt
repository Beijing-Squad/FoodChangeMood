package di

import org.beijing.presentation.FoodConsoleUi
import org.koin.dsl.module

val uiModule = module {
    single { FoodConsoleUi(get(),get(),get(),get()) }
}