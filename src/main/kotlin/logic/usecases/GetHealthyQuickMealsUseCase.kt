package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class GetHealthyQuickMealsUseCase(
    mealRepository: MealRepository
) {

    fun getHealthyQuickPreparedMeals(allMeals: List<Meal>): List<Meal> {
        val (fatAvg, saturatedFatAvg, carbsAvg) = calculateNutritionAverages(allMeals)

        return allMeals.filter { currentMeal ->
            currentMeal.minutes <= 15 && isMealHealthy(
                currentMeal,
                fatAvg,
                saturatedFatAvg,
                carbsAvg
            )
        }
    }

    private fun isMealHealthy(meal: Meal, fatAvg: Double, saturatedFatAvg: Double, carbsAvg: Double): Boolean {
        return meal.nutrition.totalFat < fatAvg &&
                meal.nutrition.saturatedFat < saturatedFatAvg &&
                meal.nutrition.carbohydrates < carbsAvg
    }

    private fun calculateAverage(values: List<Double>): Double {
        return if (values.isNotEmpty()) values.sum() / values.size else 0.0
    }

    private fun calculateNutritionAverages(meals: List<Meal>): Triple<Double, Double, Double> {
        return Triple(
            calculateAverage(meals.map { it.nutrition.totalFat }),
            calculateAverage(meals.map { it.nutrition.saturatedFat }),
            calculateAverage(meals.map { it.nutrition.carbohydrates })
        )
    }
}