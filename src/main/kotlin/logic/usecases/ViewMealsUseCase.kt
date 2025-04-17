package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal


class ViewMealsUseCase(
    private val mealRepository: MealRepository
) {
    fun getSortedSeaFoodByProtein(): List<Meal> {
        return mealRepository.getAllMeals()
            .filter { it.tags.contains("seafood") }
            .sortedByDescending { it.nutrition.protein }
    }

}