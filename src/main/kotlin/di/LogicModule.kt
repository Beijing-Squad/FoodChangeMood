package di

import org.beijing.logic.usecases.GetSeaFoodWithProteinUseCase
import org.koin.dsl.module

val logicModule = module {
    single { GetSeaFoodWithProteinUseCase(get()) }
}