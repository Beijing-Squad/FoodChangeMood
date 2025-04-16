package di

import org.beijing.logic.usecases.GetSeaFoodSortedByProteinUseCase
import org.koin.dsl.module

val logicModule = module {
    single { GetSeaFoodSortedByProteinUseCase(get()) }
}