package org.beijing.presentation.service

import org.beijing.logic.usecases.SearchMealsUseCases
import org.beijing.model.Meal
import org.koin.mp.KoinPlatform.getKoin


fun searchMealService() {
    val searchMealsUseCases: SearchMealsUseCases = getKoin().get()

    showOptionsForSearchMealService()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        1 -> launchGymHelper(searchMealsUseCases)
        2 -> launchSearchByName(searchMealsUseCases)
        0 -> return
        else -> println("Invalid input: $input")
    }
    searchMealService()
}

fun showOptionsForSearchMealService() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Gym Helper")
    println("2. Search by name of meal")
    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// region search by name
private fun launchSearchByName(searchMealsUseCases: SearchMealsUseCases) {
    val mealNameQuery = readMealNameFromInput()
    val searchResults = searchMealsUseCases.getSearchMealsByName(mealNameQuery)
    displaySearchResults(searchResults, mealNameQuery)
}

private fun readMealNameFromInput(): String {
    print("Enter meal name to search: ")
    val input = readlnOrNull()?.trim()
        ?: throw IllegalArgumentException("Meal name input cannot be null.")

    if (input.isEmpty()) {
        throw IllegalArgumentException("Meal name input cannot be empty.")
    }

    return input
}

private fun displaySearchResults(results: List<Meal>, query: String) {
    if (results.isEmpty()) {
        println("No meals found matching \"$query\".")
    } else {
        println("Meals found:")
        results.forEach { meal ->
            println(meal.name)
        }
    }
}
//endregion

// region gym helper
private fun launchGymHelper(searchMealsUseCases: SearchMealsUseCases) {
    print("enter target of Calories: ")
    val targetCalories = readlnOrNull()?.toDoubleOrNull()
    print("enter target of Protein:")
    val targetProtein = readlnOrNull()?.toDoubleOrNull()
    if (targetProtein != null && targetCalories != null) {
        println(
            searchMealsUseCases.getGymHelperMeals(
                targetCalories,
                targetProtein
            )
        )
    }
}
//endregion

// region explore country game service
fun exploreCountryGameService() {
    val searchMealsUseCase: SearchMealsUseCases = getKoin().get()
    println("🎌 Welcome to 'Explore Other Countries' Food Culture'!")
    println("------------------------------------------------------")
    println("🍱 In this mini-game, you enter a country name and discover up to 20 random meals from that region.")
    println("🌍 For example, try entering 'Italy', 'India', or 'Mexico'.")

    while (true) {
        println("\n🔎 Enter a country name (or type 'exit' to quit):")
        val country = readlnOrNull()?.trim()

        when {
            country.equals("exit", ignoreCase = true) -> {
                println("👋 Thanks for playing! Come back soon!")
                break
            }

            country.isNullOrBlank() || country.length < 4 -> {
                println("⚠️ Please enter a country name with at least 4 characters.")
                continue
            }

            country.all { it.isDigit() } -> {
                println("🚫 Please enter a valid name, not just numbers.")
                continue
            }

            else -> {
                val meals = searchMealsUseCase.exploreCountryMeals(country)
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
}
// end region explore country service
