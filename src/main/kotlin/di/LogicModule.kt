package di

import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ExploreCountryMealsUseCase
import org.koin.dsl.module

val logicModule = module {
    single { ExploreCountryMealsUseCase(get<MealRepository>()) }
}