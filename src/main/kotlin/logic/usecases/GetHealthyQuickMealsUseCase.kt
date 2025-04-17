package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class GetHealthyQuickMealsUseCase(
    mealRepository: MealRepository
) {
    private val allMeals = mealRepository.getAllMeals()
    private fun getHealthyQuickPreparedMeals(): List<Meal> {
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

    fun showHealthyQuickPreparedMeals() {
        val healthyQuickMeals = getHealthyQuickPreparedMeals()

        if (healthyQuickMeals.isEmpty()) {
            println("There is no healthy quick-prepared meals.")
        }

        println("🍕🍔🍗 List of healthy quick-prepared meals \uD83C\uDF55\uD83C\uDF54\uD83C\uDF57")
        println("-".repeat(120))
        println(
            "Rank".padEnd(5) + "| " +
                    "Meal Name".padEnd(70) + "| " +
                    "Time".padEnd(6) + "| " +
                    "Fat".padEnd(8) + "| " +
                    "Saturated Fat".padEnd(15) + "| " +
                    "Carbs"
        )
        println("-".repeat(120))

        healthyQuickMeals.forEachIndexed { index, meal ->
            println(
                "${(index + 1).toString().padEnd(5)}| " +
                        meal.name.padEnd(70) + "| " +
                        "${meal.minutes}m".padEnd(6) + "| " +
                        "${meal.nutrition.totalFat}".padEnd(8) + "| " +
                        "${meal.nutrition.saturatedFat}".padEnd(15) + "| " +
                        "${meal.nutrition.carbohydrates}"
            )
        }
    }
}