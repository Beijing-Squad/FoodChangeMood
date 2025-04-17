package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal
import kotlin.math.abs

class SearchMealsUseCases(
    private val mealRepository: MealRepository
) {

    //region iraqi meals
    fun getIraqiMeals(): List<Meal> {
        val allMeals = mealRepository.getAllMeals()

        // Filter meals that are tagged with "Iraqi" or have "Iraq" in the description
        return allMeals.filter { meal ->
            (meal.tags?.any { tag -> tag.equals("Iraqi", ignoreCase = true) } == true) ||
                    meal.description?.contains("Iraq", ignoreCase = true) == true
        }
    }
//endregion

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

    companion object {
        const val MATCH_PERCENTAGE = 0.5
        const val RATIO = 0.15
        const val ERROR_MESSAGE = "\nPlease ensure that both Calories " +
                "and Protein inputs are positive values."
    }
}