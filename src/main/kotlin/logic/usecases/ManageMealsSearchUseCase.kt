package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.utils.KmpSearch
import org.beijing.logic.usecases.utils.parseDate
import org.beijing.model.Meal
import kotlin.math.abs

class ManageMealsSearchUseCase(
    private val mealRepository: MealRepository
) {

    // region search meal by date
    fun getMealsByDate(date: String): List<Meal> {
        return mealRepository.getAllMeals()
            .filter { it.submitted == date.parseDate() }
            .ifEmpty { throw Exception("❌ No Meals Found For The Date [$date].") }
    }
    // endregion

    // region search meal by id
    fun getMealById(id: Int): Meal {
        return mealRepository.getAllMeals()
            .find { it.id == id }
            ?: throw Exception("❌ Meal with ID [$id] Not Found In The Meals List.")
    }
    // endregion

    //region search meal for gym helper by calories and protein
    fun getGymHelperMealsByCaloriesAndProtein(targetCalories: Double, targetProtein: Double): List<Meal> {
        checkIfTargetCaloriesAndTargetProteinAreInvalid(targetCalories, targetProtein)
        return mealRepository.getAllMeals()
            .filter { currentMeal ->
                isMealWithinNutritionTargets(currentMeal, targetCalories, targetProtein)
            }.ifEmpty { throw Exception("\n⚠️ No meals found!\n🍽️ Try searching again or check your filters.\n") }
    }

    private fun checkIfTargetCaloriesAndTargetProteinAreInvalid(targetCalories: Double, targetProtein: Double) {
        if (targetCalories <= 0 || targetProtein <= 0) throw Exception(
            "\nPlease ensure that both Calories " + "and Protein inputs are positive values."
        )
    }

    private fun isMealWithinNutritionTargets(meal: Meal, targetCalories: Double, targetProtein: Double): Boolean {
        return calculateNutrition(
            meal.nutrition.caloriesKcal,
            targetCalories
        ) <= MATCH_PERCENTAGE && calculateNutrition(meal.nutrition.proteinGrams, targetProtein) <= MATCH_PERCENTAGE
    }

    private fun calculateNutrition(currentNutrition: Double, targetNutrition: Double): Double {
        return abs(currentNutrition - targetNutrition) * RATIO
    }
    //endregion

    //region search meal by name
    fun getMealByName(searchQuery: String): List<Meal> {

        val allMeals = fetchAllMeals()

        return filterMealsByName(allMeals, searchQuery)
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

    //region iraqi meals
    fun getIraqiMeals(): List<Meal> {
        val allMeals = mealRepository.getAllMeals()

        return allMeals.filter { meal ->
            (meal.tags?.any { tag -> tag.equals(IRAQI, ignoreCase = true) } == true) ||
                    meal.description?.contains(IRAQI, ignoreCase = true) == true
        }
    }
    //endregion

    private companion object {
        const val MATCH_PERCENTAGE = 0.5
        const val RATIO = 0.15
        const val IRAQI = "Iraqi"
        const val BLANK_SEARCH_EXCEPTION = "Search query must not be blank."
        const val NO_FOOD_DATA = "No food data available to search."
    }
}