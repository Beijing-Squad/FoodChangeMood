package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class ManageMealsSuggestionsUseCase(
    private val mealRepository: MealRepository
) {
    private val seen = mutableSetOf<Int>()

    //region suggest sweets with no eggs
    fun suggestSweetsWithNoEggs(): Meal? {
        return mealRepository.getAllMeals().firstOrNull { meal ->
            meal.tags.any { it.contains(SWEET, ignoreCase = true) }
                    && !meal.ingredients.any { it.contains(EGG, ignoreCase = true) }
                    && !seen.contains(meal.id)
        }
            .also { it?.let { seen.add(it.id) } }
    }
    //endregion

    //region suggest ten random meals contains potato in ingredients
    fun suggestTenRandomMealsContainsPotato(): List<Meal> {
        val mealsWithPotato = mealRepository.getAllMeals().filter { meal ->
            meal.ingredients.any { ingredient ->
                ingredient.contains(POTATO, true)
            }
        }

        if (mealsWithPotato.isEmpty()) {
            throw IllegalArgumentException("There are no meals that contain potato.")
        } else if (mealsWithPotato.size < 10) {
            throw IllegalArgumentException("There are not enough $MEALS_SUGGESTION_TEN_LIMIT meals containing potato.")
        }

        return mealsWithPotato.shuffled().take(MEALS_SUGGESTION_TEN_LIMIT)
    }
    //endregion

    //region suggest italian large group meals
    fun suggestItalianLargeGroupsMeals(): List<Meal> {
        return mealRepository.getAllMeals()
            .filter { meal ->
                meal.tags.any { it.equals(FOR_LARGE_GROUP, ignoreCase = true) } &&
                        meal.tags.any { it.equals(ITALIAN, ignoreCase = true) }
            }
    }//endregion

    //region suggest keto diet meals
    fun suggestKetoMeal(usedMealIds: MutableSet<Int>): Meal? {
        val meals = mealRepository.getAllMeals()
        val maxCarbs = 20
        return meals
            .asSequence()
            .filter { meal ->
                meal.nutrition.carbohydratesGrams < maxCarbs &&
                        meal.nutrition.totalFatGrams > meal.nutrition.proteinGrams
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
            .filter { it.nSteps <= N_STEP && it.nIngredients <= N_INGREDIENTS && it.minutes <= MINUTES }
            .shuffled()
            .take(N_EASY_MEAL)
            .toList()
    }
    // endregion easy food suggestions

    // region suggest meals have more than seven hundred calories
    fun suggestMealHaveMoreThanSevenHundredCalories(): List<Meal> {

        val filteredMeals = mealRepository.getAllMeals().filter(::checkMealCaloriesContent)
        return filteredMeals
    }

    fun checkMealCaloriesContent(meal: Meal): Boolean {
        return meal.nutrition.caloriesKcal >= CALORIES_CONTENT_NEEDED
    }
    //endregion

    private companion object {
        const val CALORIES_CONTENT_NEEDED = 700
        const val N_STEP = 6
        const val N_INGREDIENTS = 5
        const val MINUTES = 30
        const val N_EASY_MEAL = 10
        const val MEALS_SUGGESTION_TEN_LIMIT = 10
        const val SWEET = "sweet"
        const val EGG = "egg"
        const val POTATO = "potato"
        const val FOR_LARGE_GROUP = "for-large-groups"
        const val ITALIAN = "italian"

    }
}