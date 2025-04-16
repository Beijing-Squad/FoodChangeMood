package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class GetTenRandomPotatoMealsUseCase(
    mealRepository: MealRepository
) {
    private val allMeals = mealRepository.getAllMeals()

    fun getTenRandomPotatoMeals(): List<Meal> {
        return getMealsWithPotatoInIngredients(allMeals).getTenRandom()
    }

    private fun getMealsWithPotatoInIngredients(meals: List<Meal>): List<Meal> {
        return meals.filter { meal ->
            meal.ingredients.any { ingredient ->
                ingredient.contains("Potato", true)
            }
        }
    }

    private fun List<Meal>.getTenRandom(): List<Meal> = this.shuffled().take(10)
}