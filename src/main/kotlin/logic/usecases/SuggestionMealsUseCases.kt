package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class SuggestionMealsUseCases(
    private val mealRepository: MealRepository
) {

    fun getMealsHaveMoreThanSevenHundredCalories(): List<Meal> {
        val caloriesContentNeeded = 700
        val filteredMeals = mealRepository.getAllMeals().filter { meal ->
            meal.nutrition.calories >= caloriesContentNeeded
        }
        return filteredMeals
    }

}