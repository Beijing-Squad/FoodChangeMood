package org.beijing.presentation

import org.beijing.logic.usecases.GetSortedSeaFoodByProteinUseCase

data class MealUseCases(
    val seaFoodWithProtein: GetSortedSeaFoodByProteinUseCase
)
