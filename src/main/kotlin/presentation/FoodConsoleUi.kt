package org.beijing.presentation

import org.beijing.presentation.service.gameMealService
import org.beijing.presentation.service.searchMealService
import org.beijing.presentation.service.suggestionMealService

class FoodConsoleUi(
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

    private fun showOptions() {
        println("\n\n ===Please enter one of the numbers listed below===\n")
        //write here your feature as string with number
        println("1. Suggestion Meal")
        println("2. Search Meal")
        println("3. Game Meal")

        println("0. Exit")
        print("\nhere: ")
    }

    private fun launchGymHelper() {
        print("enter target of Calories: ")
        val targetCalories = readlnOrNull()?.toDoubleOrNull()
        print("enter target of Protein:")
        val targetProtein = readlnOrNull()?.toDoubleOrNull()
        if (targetProtein != null && targetCalories != null) {
            println(
                mealUseCases.getMealsByCaloriesAndProteinUseCases.getMealsByCaloriesAndProtein(
                    targetCalories,
                    targetProtein
                )
            )
        }
    }


    private fun getUserInput(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }

}
