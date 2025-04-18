package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal
import org.beijing.util.Constant

class ManageMealsSuggestionsUseCases(
    private val mealRepository: MealRepository
) {
    //region sweets with no eggs
    fun getSweetWithNoEggs(): Meal? {
        val meals = mealRepository.getAllMeals()
        val seen = mutableSetOf<Int>()

        val sweetsWithoutEggs = meals
            .filter { meal ->
                meal.tags.any { it.contains("sweet", ignoreCase = true) }
            }
            .filterNot { meal ->
                meal.ingredients.any { it.contains("egg", ignoreCase = true) }
            }
            // Exclude already shown
            .filterNot { seen.contains(it.id) }

        val nextMeal = sweetsWithoutEggs.firstOrNull()
        nextMeal?.let { seen.add(it.id) }

        return nextMeal
    }
    //endregion

    //region ten random meals contains potato
    fun getTenRandomMealsContainsPotato(): List<Meal> {
        return mealRepository.getAllMeals().asSequence().filter { meal ->
            meal.ingredients.any { ingredient ->
                ingredient.contains("Potato", true)
            }
        }.shuffled().take(10).toList()
    }
    //endregion

    //region Italian Large Group Meals
    fun getItalianLargeGroupsMeals(): List<Meal> {
        return mealRepository.getAllMeals()
            .filter {
                "for-large-groups" in it.tags.map(String::lowercase) &&
                        "italian" in it.tags.map(String::lowercase)
            }
    }//endregion

    //region keto diet meal
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
    //end region

    // region easy food suggestions
    fun getEasyFoodSuggestion(): List<Meal> {
        return mealRepository.getAllMeals().asSequence()
            .filter { it.nSteps <= Constant.N_STEP && it.nIngredients <= Constant.N_INGREDIENTS && it.minutes <= Constant.MINUTES }
            .shuffled()
            .take(Constant.N_EASY_MEAL)
            .toList()
    }
    // end region easy food suggestions


    // region Meals have more than seven hundred calories

    fun suggestMealHaveMoreThanSevenHundredCalories(): List<Meal> {
        val caloriesContentNeeded = 700
        val filteredMeals = mealRepository.getAllMeals().filter { meal ->
            meal.nutrition.calories >= caloriesContentNeeded
        }
        return filteredMeals
    }
    //end region
}