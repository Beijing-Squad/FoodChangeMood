package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.utils.KmpSubstringSearch
import org.beijing.model.Meal
import kotlin.math.abs

class SearchMealsUseCases(
    private val mealRepository: MealRepository,
    private val kmpSubstringSearch: KmpSubstringSearch
) {

    //region gym helper
    fun getGymHelperMeals(targetCalories: Double, targetProtein: Double): List<Meal> {
        checkIfTargetCaloriesAndTargetProteinAreInvalid(targetCalories, targetProtein)

        return mealRepository.getAllMeals().filter { currentMeal ->
            isMealWithinNutritionTargets(currentMeal, targetCalories, targetProtein)
        }

    }

    private fun checkIfTargetCaloriesAndTargetProteinAreInvalid(targetCalories: Double, targetProtein: Double) {
        if (targetCalories <= 0 || targetProtein <= 0) throw Exception(ERROR_MESSAGE)
    }

    private fun isMealWithinNutritionTargets(meal: Meal, targetCalories: Double, targetProtein: Double): Boolean {
        return calculateNutrition(meal.nutrition.calories, targetCalories) <= MATCH_PERCENTAGE &&
                calculateNutrition(meal.nutrition.protein, targetProtein) <= MATCH_PERCENTAGE
    }

    private fun calculateNutrition(currentNutrition: Double, targetNutrition: Double): Double {
        return abs(currentNutrition - targetNutrition) * RATIO
    }

    //endregion
    
    //region search by name
    fun getSearchMealsByName(searchQuery: String): List<Meal> {
        validateSearchQuery(searchQuery)

        val allMeals = getAllMealsOrThrow()

        return filterMealsByName(allMeals, searchQuery)
    }

    private fun validateSearchQuery(query: String) {
        if (query.isBlank()) {
            throw IllegalArgumentException("Search query must not be blank.")
        }
    }

    private fun getAllMealsOrThrow(): List<Meal> {
        val meals = mealRepository.getAllMeals()
        if (meals.isEmpty()) {
            throw IllegalStateException("No food data available to search.")
        }
        return meals
    }

    private fun filterMealsByName(meals: List<Meal>, query: String): List<Meal> {
        return meals.filter { meal ->
            kmpSubstringSearch.doesTextContainPattern(
                meal.name.lowercase(),
                query.lowercase()
            )
        }
    }
    //endregion

    companion object {
        const val MATCH_PERCENTAGE = 0.5
        const val RATIO = 0.15
        const val ERROR_MESSAGE = "\nPlease ensure that both Calories " +
                "and Protein inputs are positive values."
    }
}