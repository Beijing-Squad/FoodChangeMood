package di

import org.beijing.logic.usecases.SearchMealsUseCases
import org.koin.dsl.module

val logicModule = module {
    // put features classes here
    single { SearchMealsUseCases(get(),get())}
}