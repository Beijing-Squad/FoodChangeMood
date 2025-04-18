package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class SuggestionMealsUseCases(
    private val mealRepository: MealRepository
) {

    //region Italian Large Group Meals
    fun getItalianLargeGroupsMeals(): List<Meal> {
        return mealRepository.getAllMeals()
            .filter {
                "for-large-groups" in it.tags.map(String::lowercase) &&
                        "italian" in it.tags.map(String::lowercase)
            }
    }//endregion
}