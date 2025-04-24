package org.beijing.logic.usecases

import kotlinx.datetime.LocalDate
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.utils.KmpSearch
import org.beijing.model.Meal
import kotlin.math.abs

class ManageMealsSearchUseCase(
    private val mealRepository: MealRepository
) {

    // region search meal by date then see meal details by id
    fun getMealsByDate(date: LocalDate): List<Pair<Int, String>> {
        val mealsOnDate = mealRepository.getAllMeals()
            .filter { it.submitted == date }
            .map { it.id to it.name }

        return mealsOnDate.ifEmpty { throw Exception("❌ No Meals Found For The Date [$date].") }
    }

    fun getMealByDateAndId(date: LocalDate, id: Int): Meal {
        val meal = mealRepository.getAllMeals()
            .find { it.submitted == date && it.id == id }

        return meal ?: throw Exception("❌ No Meal Found With ID [$id] On The Date $date.")
    }
    // endregion

    //region search meal for gym helper by calories and protein
    fun getGymHelperMealsByCaloriesAndProtein(targetCalories: Double, targetProtein: Double): List<Meal> {
        return mealRepository.getAllMeals().filter { currentMeal ->
            isMealWithinNutritionTargets(currentMeal, targetCalories, targetProtein)
        }
    }

    private fun isMealWithinNutritionTargets(meal: Meal, targetCalories: Double, targetProtein: Double): Boolean {
        return calculateNutrition(meal.nutrition.caloriesKcal, targetCalories) <= MATCH_PERCENTAGE &&
                calculateNutrition(meal.nutrition.proteinGrams, targetProtein) <= MATCH_PERCENTAGE
    }

    private fun calculateNutrition(currentNutrition: Double, targetNutrition: Double): Double {
        return abs(currentNutrition - targetNutrition) * RATIO
    }
    //endregion

    //region search meal by name
    fun getMealByName(searchQuery: String): List<Meal> {
        validateSearchQuery(searchQuery)

        val allMeals = fetchAllMeals()

        return filterMealsByName(allMeals, searchQuery)
    }

    private fun validateSearchQuery(query: String) {
        if (query.isBlank()) {
            throw IllegalArgumentException(BLANK_SEARCH_EXCEPTION)
        }
    }

    private fun fetchAllMeals(): List<Meal> {
        val meals = mealRepository.getAllMeals()
        if (meals.isEmpty()) {
            throw IllegalStateException(NO_FOOD_DATA)
        }
        return meals
    }

    private fun filterMealsByName(meals: List<Meal>, query: String): List<Meal> {
        return meals.filter { meal ->
            KmpSearch.containsPattern(
                meal.name.lowercase(),
                query.lowercase()
            )
        }
    }
    //endregion

    // region search meal by country
    fun getMealByCountry(countryQuery: String): List<Meal> {
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
    // endregion search meal by country

    // end search meal by country

    //region iraqi meals
    fun getIraqiMeals(): List<Meal> {
        return mealRepository.getAllMeals().filter { meal ->
            meal.tags?.any { it.equals(IRAQI, ignoreCase = true) } ?: false ||
                    meal.description?.contains(IRAQI, ignoreCase = true) ?: false
        }

    }
    //endregion

    private companion object {
        const val MATCH_PERCENTAGE = 0.5
        const val RATIO = 0.15
        const val IRAQI = "Iraqi"
        const val ERROR_MESSAGE = "\nPlease ensure that both Calories " +
                "and Protein inputs are positive values."
        const val BLANK_SEARCH_EXCEPTION = "Search query must not be blank."
        const val NO_FOOD_DATA = "No food data available to search."
    }
}