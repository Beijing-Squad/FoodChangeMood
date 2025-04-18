package org.beijing.presentation

import org.beijing.logic.usecases.GamesMealsUseCases
import org.beijing.logic.usecases.SearchMealsUseCases
import org.beijing.logic.usecases.SuggestionMealsUseCases
import org.beijing.logic.usecases.ViewMealsUseCases
import org.beijing.presentation.service.*

class FoodConsoleUi(
    private val viewMealsUseCases: ViewMealsUseCases,
    private val gamesMealsUseCases: GamesMealsUseCases,
    private val searchMealsUseCases: SearchMealsUseCases,
    private val suggestionMealsUseCases: SuggestionMealsUseCases
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
            5 -> exploreCountryGameService()
            6 -> easyMealService()

            else -> {
                println("Invalid input: $input")
            }
        }

        presentFeatures()
    }

    private fun onGameMealClick() {
        gameMealService(gamesMealsUseCases)
    }

    private fun onSearchMealClick() {
        searchMealService(searchMealsUseCases)
    }

    private fun onSuggestionMealClick() {
        suggestionMealService(suggestionMealsUseCases)
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
        println("5. Explore meals from a specific country")
        println("6. Easy Food Suggestion")
        print("\nhere: ")
    }

    private fun getUserInput(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }

}
