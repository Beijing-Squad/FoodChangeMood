package org.beijing.presentation.service

import org.beijing.logic.usecases.ViewMealsUseCases


fun viewMealsService(viewMealsUseCases: ViewMealsUseCases) {

    showOptionsForViewMealsService()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        // add number of feature here as ( 1-> featureOne() )
        1 -> launchHealthyQuickPreparedMeals(viewMealsUseCases)

        0 -> return

        else -> println("Invalid input: $input")
    }
    viewMealsService(viewMealsUseCases)

}

fun showOptionsForViewMealsService() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Show Healthy Quick Prepared Meals") // add feature name here


    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// add ui feature function inside region block
// region healthy fast food meals
fun launchHealthyQuickPreparedMeals(viewMealsUseCases: ViewMealsUseCases) {
    val healthyQuickMeals = viewMealsUseCases.getHealthyQuickPreparedMeals()

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

// region Sorted SeaFood


// endregion