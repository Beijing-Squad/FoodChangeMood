package di

import logic.SearchMealsByNameUseCase
import org.koin.dsl.module

val logicModule = module {
    // put features classes here

    single { SearchMealsByNameUseCase(get(),get()) }
}