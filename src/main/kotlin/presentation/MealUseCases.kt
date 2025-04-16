package org.beijing.presentation

import org.beijing.logic.usecases.GetSeaFoodSortedByProteinUseCase

data class MealUseCases(
    val seaFoodWithProtein: GetSeaFoodSortedByProteinUseCase
)
