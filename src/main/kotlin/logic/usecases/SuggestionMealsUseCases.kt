package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal
import org.beijing.util.Constant

class SuggestionMealsUseCases(
    private val mealRepository: MealRepository
) {

    // region Italian Large Group Meals
    fun getItalianLargeGroupsMeals(): List<Meal> {
        return mealRepository.getAllMeals()
            .filter {
                "for-large-groups" in it.tags.map(String::lowercase) &&
                        "italian" in it.tags.map(String::lowercase)
            }
    }// endregion
    // region easy food suggestions
    fun easyFoodSuggestion(): List<Meal> {
        return mealRepository.getAllMeals().asSequence()
            .filter { it.nSteps <= Constant.N_STEP && it.nIngredients <= Constant.N_INGREDIENTS && it.minutes <= Constant.MINUTES }
            .shuffled()
            .take(Constant.N_EASY_MEAL)
            .toList()
    }
    // end region easy food suggestions
}