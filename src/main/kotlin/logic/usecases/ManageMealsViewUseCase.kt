package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class ManageMealsViewUseCase(
    private val mealRepository: MealRepository
) {

    // region get a list of healthy fast food meals
    fun getHealthyQuickPreparedMeals(): List<Meal> {
        val (fatAvg, saturatedFatAvg, carbsAvg) = calculateNutritionAverages(mealRepository.getAllMeals())

        return mealRepository.getAllMeals().asSequence().filter { currentMeal ->
            currentMeal.minutes <= 15 && isMealHealthy(
                currentMeal,
                fatAvg,
                saturatedFatAvg,
                carbsAvg
            )
        }.toList()
    }

    private fun isMealHealthy(meal: Meal, fatAvg: Double, saturatedFatAvg: Double, carbsAvg: Double): Boolean {
        return meal.nutrition.totalFatGrams < fatAvg &&
                meal.nutrition.saturatedFatGrams < saturatedFatAvg &&
                meal.nutrition.carbohydratesGrams < carbsAvg
    }

    private fun calculateAverage(values: List<Double>): Double {
        return if (values.isNotEmpty()) values.sum() / values.size else 0.0
    }

    private fun calculateNutritionAverages(meals: List<Meal>): Triple<Double, Double, Double> {
        return Triple(
            calculateAverage(meals.map { it.nutrition.totalFatGrams }),
            calculateAverage(meals.map { it.nutrition.saturatedFatGrams }),
            calculateAverage(meals.map { it.nutrition.carbohydratesGrams })
        )
    }
    //endregion

    // region get a list of seafood sorted by protein content

    fun getSortedSeaFoodByProtein(): List<Meal> {
        return mealRepository.getAllMeals()
            .filter { it.tags.contains("seafood") }
            .sortedByDescending { it.nutrition.proteinGrams }
    }
    //endregion
}