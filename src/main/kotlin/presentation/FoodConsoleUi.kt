package org.beijing.presentation

import org.beijing.logic.usecases.ViewMealsUseCases
import org.beijing.presentation.service.*

class FoodConsoleUi(
    private val viewMealsUseCases: ViewMealsUseCases,
) {

    fun start() {
        showWelcome()

        try {
            presentFeatures()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun showWelcome() {
        println("Welcome to Food Change Mood App")
    }

    private fun presentFeatures() {
        showOptions()
        when (val input = getUserInput()) {
            0 -> return
            1 -> onSuggestionMealClick()
            2 -> onSearchMealClick()
            3 -> onGameMealClick()
            4 -> onViewMealClick()

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
        viewMealsService(viewMealsUseCases)
    }

    private fun showOptions() {
        println("\n\n ===Please enter one of the numbers listed below===\n")
        println("0. Exit")
        println("1. Suggestion Meal")
        println("2. Search Meal")
        println("3. Game Meal")
        println("4. View Meal")
        print("\nhere: ")
    }

    private fun getUserInput(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }
}