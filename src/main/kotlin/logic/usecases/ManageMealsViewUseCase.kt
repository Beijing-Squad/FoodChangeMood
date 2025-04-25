package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class ManageMealsViewUseCase(
    private val mealRepository: MealRepository
) {

    // region get a list of healthy fast food meals
    fun getHealthyQuickPreparedMeals(): List<Meal> {
        val (fatAvg, saturatedFatAvg, carbsAvg) = calculateNutritionAverages(mealRepository.getAllMeals())

        return mealRepository.getAllMeals().filter { meal ->
            meal.minutes <= MINUTES_15
                    && meal.nutrition.totalFatGrams < fatAvg
                    && meal.nutrition.saturatedFatGrams < saturatedFatAvg
                    && meal.nutrition.carbohydratesGrams < carbsAvg
        }
    }

    private fun calculateNutritionAverages(meals: List<Meal>): Triple<Double, Double, Double> {
        return Triple(
            meals.map { it.nutrition.totalFatGrams }.average(),
            meals.map { it.nutrition.saturatedFatGrams }.average(),
            meals.map { it.nutrition.carbohydratesGrams }.average()
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

    private companion object {
        const val MINUTES_15 = 15
    }
}