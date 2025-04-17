package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal
import org.beijing.model.Nutrition

class SuggestionMealsUseCases(
    private val mealRepository: MealRepository
) {
    private val meals = mealRepository.getAllMeals()
    fun suggestKetoMeal(usedMealIds: MutableSet<Int>): Meal? {
        return meals
            .filter { meal ->
                meal.nutrition.carbohydrates < 20 &&
                        meal.nutrition.totalFat > meal.nutrition.protein
            }
            .filterNot { it.id in usedMealIds }
            .shuffled()
            .firstOrNull()
            ?.also { usedMealIds.add(it.id) }
    }
}