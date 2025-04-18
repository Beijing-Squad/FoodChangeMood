package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class SuggestionMealsUseCases(
    private val mealRepository: MealRepository
) {

    //region ten random meals contains potato
    fun getTenRandomMealsContainsPotato(): List<Meal> {
        return mealRepository.getAllMeals().asSequence().filter { meal ->
            meal.ingredients.any { ingredient ->
                ingredient.contains("Potato", true)
            }
        }.shuffled().take(10).toList()
    }
    //endregion

    //region Italian Large Group Meals
    fun getItalianLargeGroupsMeals(): List<Meal> {
        return mealRepository.getAllMeals()
            .filter {
                "for-large-groups" in it.tags.map(String::lowercase) &&
                        "italian" in it.tags.map(String::lowercase)
            }
    }//endregion
}