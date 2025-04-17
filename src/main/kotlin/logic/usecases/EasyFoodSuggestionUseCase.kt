package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class EasyFoodSuggestionUseCase(
    private val mealRepository: MealRepository
){
    fun easyFoodSuggestion(): List<Meal> {
        return mealRepository.getAllMeals() .asSequence()
            .filter { it.nSteps <= 6 && it.nIngredients <= 5 && it.minutes <= 30 }
            .shuffled()
            .take(10)
            .toList()
    }
}