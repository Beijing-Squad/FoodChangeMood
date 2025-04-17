package di

import org.beijing.logic.usecases.GetHealthyQuickMealsUseCase
import org.koin.dsl.module

val logicModule = module {
    // put features classes here
    single { GetHealthyQuickMealsUseCase(get()) }

}