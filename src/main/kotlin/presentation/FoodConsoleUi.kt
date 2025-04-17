package org.beijing.presentation

import org.beijing.logic.usecases.MealUseCases
import org.beijing.presentation.service.gameMealService
import org.beijing.presentation.service.searchMealService
import org.beijing.presentation.service.suggestionMealService

class FoodConsoleUi(
    private val mealUseCases: MealUseCases
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
            //write here your feature
            1 -> onSuggestionMealClick()
            2 -> onSearchMealClick()
            3 -> onGameMealClick()
            4 -> runExploreCountryGame()

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
        println("0. Exit")
        //write here your feature as string with number
        println("1. Suggestion Meal")
        println("2. Search Meal")
        println("3. Game Meal")
        println("4. Explore meals from a specific country")
        print("\nhere: ")
    }


    private fun getUserInput(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }

    private fun runExploreCountryGame() {
        println("🎌 Welcome to 'Explore Other Countries' Food Culture'!")
        println("------------------------------------------------------")
        println("🍱 In this mini-game, you enter a country name and discover up to 20 random meals from that region.")
        println("🌍 For example, try entering 'Italy', 'India', or 'Mexico'.")

        while (true) {
            println("\n🔎 Enter a country name (or type 'exit' to quit):")
            val country = readlnOrNull()?.trim()

            if (country.equals("exit", ignoreCase = true)) {
                println("👋 Thanks for playing! Come back soon!")
                break
            }

            if (country.isNullOrBlank()) {
                println("⚠️ Please enter a valid country name.")
                continue
            }

            val meals = mealUseCases.exploreCountryMealsUseCase.exploreCountryMeals(country)

            if (meals.isEmpty()) {
                println("😔 Sorry, no meals found for '$country'. Try another country!")
            } else {
                println("\n🍽️ Found ${meals.size} meal(s) related to '$country':\n")
                meals.forEachIndexed { index, meal ->
                    println("${index + 1}. ${meal.name} • ⏱️ ${meal.minutes} mins • 🧂 ${meal.nIngredients} ingredients • 🔧 ${meal.nSteps} steps")
                }
            }
        }
    }
}
