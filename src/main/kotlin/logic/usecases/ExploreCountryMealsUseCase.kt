package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class ExploreCountryMealsUseCase(
    private val mealRepository: MealRepository
) {
    fun exploreCountryMeals(countryQuery: String): List<Meal> {
        val query = countryQuery.lowercase()
        return mealRepository.getAllMeals()
            .asSequence()
            .filter { meal ->
                meal.tags.any {
                    it.lowercase().contains(query)
                    meal.tags.joinToString(" ").lowercase().contains(query)
                }
            }
            .shuffled()
            .take(20)
            .toList()
    }
}
