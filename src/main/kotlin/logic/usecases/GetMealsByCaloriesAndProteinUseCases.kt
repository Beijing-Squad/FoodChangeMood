package logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal
import kotlin.math.abs

class GetMealsByCaloriesAndProteinUseCases(
    private val mealRepository: MealRepository
) {
    fun getMealsByCaloriesAndProtein(targetCalories: Double, targetProtein: Double): List<Meal> {
        if (targetCalories <= 0) throw Exception("Calories input must be positive")
        if (targetProtein <= 0) throw Exception("Protein input must be positive")

        val matchPercentage = 0.5
        val ratio = 0.15
        return mealRepository.getAllMeals().filter { currentMeal ->
            abs(currentMeal.nutrition.calories - targetCalories) * ratio <= matchPercentage &&
                    abs(currentMeal.nutrition.protein - targetProtein) * ratio <= matchPercentage
        }

    }
}