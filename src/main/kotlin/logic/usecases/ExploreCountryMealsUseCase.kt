package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class ExploreCountryMealsUseCase(
    private val mealRepository: MealRepository
) {
    fun exploreCountryMeals(countryQuery: String): List<Meal> {
        val meals = mealRepository.getAllMeals()
        val query = countryQuery.lowercase()
        return meals?.filter { meal ->
            meal.name.lowercase().contains(query) ||
                    meal.tags.joinToString(" ").lowercase().contains(query) ||
                    (meal.description?.lowercase()?.contains(query) ?: false) ||
                    meal.ingredients.joinToString(" ").lowercase().contains(query)
        }?.shuffled()?.take(20) ?: emptyList()
    }
}

