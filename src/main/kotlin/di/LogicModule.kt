package di

import logic.usecases.GetMealsByCaloriesAndProteinUseCases
import org.koin.dsl.module

val logicModule = module {
    // put features classes here
    single { GetMealsByCaloriesAndProteinUseCases(get()) }
}