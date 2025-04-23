package org.beijing.presentation


import org.beijing.presentation.service.*
import presentation.view_read.ConsoleIO

class FoodConsoleUi(
    private val searchMealService: SearchMealService,
    private val viewMealsService: ViewMealsService,
    private val gameMealsService: GameMealsService,
    private val suggestionMealsService: SuggestionMealsService,
    private val consoleIO: ConsoleIO
) : FoodUi {
    override fun start() {
        showWelcome()

        try {
            presentFeatures()
        } catch (e: Exception) {
            consoleIO.println(e.message)
        }
    }

    private fun showWelcome() {
        consoleIO.print("Welcome to Food Change Mood\uD83E\uDD6A ")
    }

    private fun presentFeatures() {
        showOptions()
        when (consoleIO.readInput()) {
            "1" -> suggestionMealsService.showService()
            "2" -> searchMealService.showService()
            "3" -> gameMealsService.showService()
            "4" -> viewMealsService.showService()
            "0" -> return
            else -> {
                consoleIO.println("❌ Invalid input! Please enter a number between 0 and 4")
            }
        }
        presentFeatures()
    }

    private fun showOptions() {
        consoleIO.println("\n\n===Please enter one of the numbers listed below===\n")
        consoleIO.println("1. Suggestion Meal \uD83E\uDD14")
        consoleIO.println("2. Search Meal \uD83D\uDD0E")
        consoleIO.println("3. Game Meal \uD83C\uDFAE")
        consoleIO.println("4. View Meal \uD83E\uDD63")
        consoleIO.println("0. Exit")
        consoleIO.print("\nhere: ")
    }

}
