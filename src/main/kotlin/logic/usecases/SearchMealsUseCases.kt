package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.utils.KmpSubstringSearch
import org.beijing.model.Meal

class SearchMealsUseCases(
    private val mealRepository: MealRepository,
    private val kmpSubstringSearch: KmpSubstringSearch
) {
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
}