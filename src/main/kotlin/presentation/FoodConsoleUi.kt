package org.beijing.presentation


import org.beijing.presentation.service.*

class FoodConsoleUi(
    private val searchMealService: SearchMealService,
    private val viewMealsService: ViewMealsService,
    private val gameMealsService: GameMealsService
) : FoodUi {
    override fun start() {
        showWelcome()

        try {
            presentFeatures()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun showWelcome() {
        print("Welcome to Food Change Mood\uD83E\uDD6A ")
    }

    private fun presentFeatures() {
        showOptions()
        when (getUserInput()) {
            1 -> suggestionMealService()
            2 -> searchMealService.showService()
            3 -> gameMealsService.showService()
            4 -> viewMealsService.showService()
            0 -> return
            else -> {
                println("❌ Invalid input! Please enter a number between 0 and 4")
            }
        }
        presentFeatures()
    }

    private fun showOptions() {
        println("\n\n===Please enter one of the numbers listed below===\n")
        println("1. Suggestion Meal \uD83E\uDD14")
        println("2. Search Meal \uD83D\uDD0E")
        println("3. Game Meal \uD83C\uDFAE")
        println("4. View Meal \uD83E\uDD63")
        println("0. Exit")
        print("\nhere: ")
    }


    private fun getUserInput(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }

}
