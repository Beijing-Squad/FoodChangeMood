package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal
import org.beijing.util.Constant

class ManageMealsSuggestionsUseCase(
    private val mealRepository: MealRepository
) {

    //region suggest sweets with no eggs
    fun suggestSweetsWithNoEggs(): Meal? {
        val meals = mealRepository.getAllMeals()
        val seen = mutableSetOf<Int>()
        val sweetsWithoutEggs = meals
            .filter { meal ->
                meal.tags.any { it.contains("sweet", ignoreCase = true) }
            }
            .filterNot { meal ->
                meal.ingredients.any { it.contains("egg", ignoreCase = true) }
            }
            .filterNot { seen.contains(it.id) }

        val nextMeal = sweetsWithoutEggs.firstOrNull()
        nextMeal?.let { seen.add(it.id) }

        return nextMeal
    }
    //endregion

    //region suggest ten random meals contains potato in ingredients
    fun suggestTenRandomMealsContainsPotato(): List<Meal> {
        return mealRepository.getAllMeals().asSequence().filter { meal ->
            meal.ingredients.any { ingredient ->
                ingredient.contains("Potato", true)
            }
        }.shuffled().take(10).toList()
    }
    //endregion

    //region suggest italian large group meals
    fun suggestItalianLargeGroupsMeals(): List<Meal> {
        return mealRepository.getAllMeals()
            .filter {
                "for-large-groups" in it.tags.map(String::lowercase) &&
                        "italian" in it.tags.map(String::lowercase)
            }
    }//endregion

    //region suggest keto diet meals
    fun suggestKetoMeal(usedMealIds: MutableSet<Int>): Meal? {
        val meals = mealRepository.getAllMeals()
        val maxCarbs = 20
        return meals
            .asSequence()
            .filter { meal ->
                meal.nutrition.carbohydrates < maxCarbs &&
                        meal.nutrition.totalFat > meal.nutrition.protein
            }
            .filterNot { it.id in usedMealIds }
            .shuffled()
            .firstOrNull()
            ?.also { usedMealIds.add(it.id) }
    }
    //endregion

    // region suggest easy prepared meal
    fun suggestEasyPreparedMeal(): List<Meal> {
        return mealRepository.getAllMeals().asSequence()
            .filter { it.nSteps <= Constant.N_STEP && it.nIngredients <= Constant.N_INGREDIENTS && it.minutes <= Constant.MINUTES }
            .shuffled()
            .take(Constant.N_EASY_MEAL)
            .toList()
    }
    // endregion easy food suggestions

    // region suggest meals have more than seven hundred calories
    fun suggestMealHaveMoreThanSevenHundredCalories(): List<Meal> {

        val filteredMeals = mealRepository.getAllMeals().filter(::checkMealCaloriesContent)
        return filteredMeals
    }

    fun checkMealCaloriesContent(meal: Meal): Boolean {
        return meal.nutrition.calories >= CALORIES_CONTENT_NEEDED
    }
    //endregion

    private companion object {
        val CALORIES_CONTENT_NEEDED = 700

    }
}