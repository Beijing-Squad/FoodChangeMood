package org.beijing.presentation.service

import org.beijing.logic.usecases.ManageMealsViewsUseCases
import org.koin.mp.KoinPlatform.getKoin

private val viewMeals: ManageMealsViewsUseCases = getKoin().get()

fun viewMealsService() {

    showOptionsForViewMealsService()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        1 -> launchHealthyQuickPreparedMeals()
        2 -> showSortedSeaFoodByProtein()
        0 -> return

        else -> println("Invalid input: $input")
    }
    viewMealsService()
}

fun showOptionsForViewMealsService() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Show Healthy Quick Prepared Meals") // add feature name here
    println("1. Show SeaFood Sorted By Protein Content")

    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// region healthy fast food meals
fun launchHealthyQuickPreparedMeals() {
    val healthyQuickMeals = viewMeals.getHealthyQuickPreparedMeals()

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

// region get a list of seafood sorted by protein content

fun showSortedSeaFoodByProtein() {
    println("List Of SeaFood Sorted By Protein:")
    println(String.format("%-6s| %-70s | %-14s", "Rank", "Meal Name", "Protein Content"))
    println("----------------------------------------------------------------------------------------------------")
    viewMeals.getSortedSeaFoodByProtein().forEachIndexed { index, meal ->
        println(String.format("%-6d| %-70s | %-14d", index + 1, meal.name, meal.nutrition.protein.toInt()))
    }
}
//endregion