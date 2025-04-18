package org.beijing.presentation

import org.beijing.logic.usecases.GamesMealsUseCases
import org.beijing.logic.usecases.SearchMealsUseCases
import org.beijing.logic.usecases.SuggestionMealsUseCases
import org.beijing.logic.usecases.ViewMealsUseCases
import org.beijing.presentation.service.gameMealService
import org.beijing.presentation.service.searchMealService
import org.beijing.presentation.service.suggestionMealService
import org.beijing.presentation.service.viewMealsService

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
            //write here your feature
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
        viewMealsService(viewMealsUseCases)
    }

    private fun showOptions() {
        println("\n\n ===Please enter one of the numbers listed below===\n")
        //write here your feature as string with number
        println("1. Suggestion Meal")
        println("2. Search Meal")
        println("3. Game Meal")
        println("4. View Meal")

        println("0. Exit")
        print("\nhere: ")
    }


    private fun getUserInput(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }

}
