package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class SuggestionMealsUseCases(
    private val mealRepository: MealRepository
) {
    //region sweets with no eggs
    fun getSweetWithNoEggs(): Meal? {
        val meals = mealRepository.getAllMeals()
        val seen = mutableSetOf<Int>()

        val sweetsWithoutEggs = meals
            .filter { meal ->
                meal.tags.any { it.contains("sweet", ignoreCase = true) }
            }
            .filterNot { meal ->
                meal.ingredients.any { it.contains("egg", ignoreCase = true) }
            }
            // Exclude already shown
            .filterNot { seen.contains(it.id) }

        val nextMeal = sweetsWithoutEggs.firstOrNull()
        nextMeal?.let { seen.add(it.id) }

        return nextMeal
    }
    //endregion
}