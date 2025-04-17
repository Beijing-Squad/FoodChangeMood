package di

import org.beijing.logic.usecases.SearchMealsUseCases
import org.beijing.logic.usecases.utils.KmpSubstringSearch
import org.koin.dsl.module

val logicModule = module {
    // put features classes here

    single { KmpSubstringSearch() }
    single { SearchMealsUseCases(get(),get())}
}