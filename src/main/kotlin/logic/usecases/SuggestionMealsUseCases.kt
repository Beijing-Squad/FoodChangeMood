package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class SuggestionMealsUseCases(
    private val mealRepository: MealRepository
) {
    private val meals = mealRepository.getAllMeals()
    fun suggestKetoMeal(usedMealIds: MutableSet<Int>): Meal? {
        val maxCarbs = 20
        return meals
            .asSequence()
            .filter { meal ->
                meal.nutrition.carbohydrates < maxCarbs &&
                meal.nutrition.totalFat > meal.nutrition.protein
            }
            .filterNot { it.id in usedMealIds }
            .shuffled()
            .firstOrNull()
            ?.also { usedMealIds.add(it.id) }
    }
}