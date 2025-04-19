package org.beijing.presentation


import org.beijing.presentation.service.*

class FoodConsoleUi {
    fun start() {
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
        when (val input = getUserInput()) {
            1 -> onSuggestionMealClick()
            2 -> onSearchMealClick()
            3 -> onGameMealClick()
            4 -> onViewMealClick()
            0 -> return

            else -> {
                println("Invalid input: $input")
            }
        }
        presentFeatures()
    }

    private fun onGameMealClick() {
        gameMealService()
    }

    private fun onSearchMealClick() {
        searchMealService()
    }

    private fun onSuggestionMealClick() {
        suggestionMealService()
    }

    private fun onViewMealClick() {
        viewMealsService()
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
