package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal
import org.beijing.util.Constant

class EasyMealSuggestionUseCase(
    private val mealRepository: MealRepository
){
    fun easyFoodSuggestion(): List<Meal> {
        return mealRepository.getAllMeals() .asSequence()
            .filter { it.nSteps <= Constant.N_STEP && it.nIngredients <= Constant.N_INGREDIENTS && it.minutes <= Constant.MINUTES }
            .shuffled()
            .take(Constant.N_EASY_MEAL)
            .toList()
    }
}