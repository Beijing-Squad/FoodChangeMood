package di

import org.beijing.logic.IngredientGameUseCase
import org.koin.dsl.module

val logicModule = module {
    // put features classes here
    single { IngredientGameUseCase(get()) }
}