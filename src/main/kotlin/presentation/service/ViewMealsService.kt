package org.beijing.presentation.service

import org.beijing.logic.usecases.ManageMealsViewUseCase
import presentation.view_read.ConsoleIO

class ViewMealsService(
    private val viewMeals: ManageMealsViewUseCase,
    private val consoleIO: ConsoleIO
) : MealService(consoleIO) {

    override fun showService() {
        super.showService()
        showService()
    }

    override fun showOptionService() {
        consoleIO.println("\n\n ===Please enter one of the numbers listed below===\n")
        consoleIO.println("1. Show Healthy Quick Prepared Meals")
        consoleIO.println("2. Show SeaFood Sorted By Protein Content")
        consoleIO.println("0. Exit")
    }

    override fun handleUserChoice() {
        consoleIO.print("\nhere: ")
        when (consoleIO.readInput()) {
            "1" -> launchHealthyQuickPreparedMeals()
            "2" -> showSortedSeaFoodByProtein()
            "0" -> return
            else -> consoleIO.println("❌ Invalid input! Please enter a number between 0 and 2")
        }
    }

    // region healthy fast food meals
    fun launchHealthyQuickPreparedMeals() {
        val healthyQuickMeals = viewMeals.getHealthyQuickPreparedMeals()

        if (healthyQuickMeals.isEmpty()) {
            consoleIO.println("There is no healthy quick-prepared meals.")
        } else {
            consoleIO.println("🍕🍔🍗 List of healthy quick-prepared meals \uD83C\uDF55\uD83C\uDF54\uD83C\uDF57")
            consoleIO.println("-".repeat(120))
            consoleIO.println(
                "Rank".padEnd(5) + "| " +
                        "Meal Name".padEnd(70) + "| " +
                        "Time".padEnd(6) + "| " +
                        "Fat".padEnd(8) + "| " +
                        "Saturated Fat".padEnd(15) + "| " +
                        "Carbs"
            )
            consoleIO.println("-".repeat(120))

            healthyQuickMeals.forEachIndexed { index, meal ->
                consoleIO.println(
                    "${(index + 1).toString().padEnd(5)}| " +
                            meal.name.padEnd(70) + "| " +
                            "${meal.minutes}m".padEnd(6) + "| " +
                            "${meal.nutrition.totalFatGrams}".padEnd(8) + "| " +
                            "${meal.nutrition.saturatedFatGrams}".padEnd(15) + "| " +
                            "${meal.nutrition.carbohydratesGrams}"
                )
            }
        }
    }
// endregion

// region get a list of seafood sorted by protein content

    fun showSortedSeaFoodByProtein() {
        consoleIO.println("List Of SeaFood Sorted By Protein:")
        consoleIO.println(String.format("%-6s| %-70s | %-14s", "Rank", "Meal Name", "Protein Content"))
        consoleIO.println("----------------------------------------------------------------------------------------------------")
        viewMeals.getSortedSeaFoodByProtein().forEachIndexed { index, meal ->
            consoleIO.println(String.format("%-6d| %-70s | %-14d", index + 1, meal.name, meal.nutrition.proteinGrams.toInt()))
        }
    }
//endregion

}