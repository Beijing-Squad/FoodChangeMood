package org.beijing.logic.usecases

import kotlinx.datetime.LocalDate
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.utils.KmpSubstringSearch
import org.beijing.model.Meal
import kotlin.math.abs

class ManageMealsSearchUseCase(
    private val mealRepository: MealRepository
) {

    // region search by add date and see meal details by id
    fun getMealsByDate(date: LocalDate): List<Pair<Int, String>> {
        val mealsOnDate = mealRepository.getAllMeals()
            .filter { it.submitted == date }
            .map { it.id to it.name }

        return mealsOnDate.ifEmpty { throw Exception("❌ No Meals Found For The Date [$date].") }
    }

    fun getMealOnDateById(date: LocalDate, id: Int): Meal {
        val meal = mealRepository.getAllMeals()
            .find { it.submitted == date && it.id == id }

        return meal ?: throw Exception("❌ No Meal Found With ID [$id] On The Date $date.")
    }
    // endregion

    //region gym helper
    fun getGymHelperMeals(targetCalories: Double, targetProtein: Double): List<Meal> {
        return mealRepository.getAllMeals().filter { currentMeal ->
            isMealWithinNutritionTargets(currentMeal, targetCalories, targetProtein)
        }
    }

    private fun isMealWithinNutritionTargets(meal: Meal, targetCalories: Double, targetProtein: Double): Boolean {
        return calculateNutrition(meal.nutrition.calories, targetCalories) <= MATCH_PERCENTAGE &&
                calculateNutrition(meal.nutrition.protein, targetProtein) <= MATCH_PERCENTAGE
    }

    private fun calculateNutrition(currentNutrition: Double, targetNutrition: Double): Double {
        return abs(currentNutrition - targetNutrition) * RATIO
    }
    //endregion

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
            KmpSubstringSearch.doesTextContainPattern(
                meal.name.lowercase(),
                query.lowercase()
            )
        }
    }
    //endregion

    // region search meal by country
    fun searchMealByCountry(countryQuery: String): List<Meal> {
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
    // end search meal by country
    companion object {
        const val MATCH_PERCENTAGE = 0.5
        const val RATIO = 0.15
    }
}