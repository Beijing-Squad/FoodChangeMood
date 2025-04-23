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
            consoleIO.viewWithLine(e.message)
        }
    }

    private fun showWelcome() {
        consoleIO.view("Welcome to Food Change Mood\uD83E\uDD6A ")
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
                consoleIO.viewWithLine("❌ Invalid input! Please enter a number between 0 and 4")
            }
        }
        presentFeatures()
    }

    private fun showOptions() {
        consoleIO.viewWithLine("\n\n===Please enter one of the numbers listed below===\n")
        consoleIO.viewWithLine("1. Suggestion Meal \uD83E\uDD14")
        consoleIO.viewWithLine("2. Search Meal \uD83D\uDD0E")
        consoleIO.viewWithLine("3. Game Meal \uD83C\uDFAE")
        consoleIO.viewWithLine("4. View Meal \uD83E\uDD63")
        consoleIO.viewWithLine("0. Exit")
        consoleIO.view("\nhere: ")
    }

}
