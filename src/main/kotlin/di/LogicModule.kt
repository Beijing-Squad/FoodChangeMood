package di

import org.beijing.logic.usecases.GetSortedSeaFoodByProteinUseCase
import org.koin.dsl.module

val logicModule = module {
    single { GetSortedSeaFoodByProteinUseCase(get()) }
}