package org.beijing.presentation.service

import org.beijing.logic.usecases.SearchMealsUseCases

fun searchMealService(searchMealsUseCases: SearchMealsUseCases) {

    showOptions()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        1 -> launchGymHelper(searchMealsUseCases)

        3 -> launchHealthyQuickPreparedMeals(searchMealsUseCases)

        0 -> return

        else -> println("Invalid input: $input")
    }
    searchMealService(searchMealsUseCases)
}

fun showOptions() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Gym Helper")
    println("3. HealthyQuickPreparedMeals")
    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// region get a list of healthy fast food meals
fun launchHealthyQuickPreparedMeals(searchMealsUseCases: SearchMealsUseCases) {
    val healthyQuickMeals = searchMealsUseCases.getHealthyQuickPreparedMeals()

    if (healthyQuickMeals.isEmpty()) {
        println("There is no healthy quick-prepared meals.")
    } else {
        println("🍕🍔🍗 List of healthy quick-prepared meals \uD83C\uDF55\uD83C\uDF54\uD83C\uDF57")
        println("-".repeat(120))
        println(
            "Rank".padEnd(5) + "| " +
                    "Meal Name".padEnd(70) + "| " +
                    "Time".padEnd(6) + "| " +
                    "Fat".padEnd(8) + "| " +
                    "Saturated Fat".padEnd(15) + "| " +
                    "Carbs"
        )
        println("-".repeat(120))

        healthyQuickMeals.forEachIndexed { index, meal ->
            println(
                "${(index + 1).toString().padEnd(5)}| " +
                        meal.name.padEnd(70) + "| " +
                        "${meal.minutes}m".padEnd(6) + "| " +
                        "${meal.nutrition.totalFat}".padEnd(8) + "| " +
                        "${meal.nutrition.saturatedFat}".padEnd(15) + "| " +
                        "${meal.nutrition.carbohydrates}"
            )
        }
    }
}
// endregion

// region gym helper
private fun launchGymHelper(searchMealsUseCases: SearchMealsUseCases) {
    print("enter target of Calories: ")
    val targetCalories = readlnOrNull()?.toDoubleOrNull()
    print("enter target of Protein:")
    val targetProtein = readlnOrNull()?.toDoubleOrNull()
    if (targetProtein != null && targetCalories != null) {
        println(
            searchMealsUseCases.getGymHelperMeals(
                targetCalories,
                targetProtein
            )
        )
    }
}
//endregion