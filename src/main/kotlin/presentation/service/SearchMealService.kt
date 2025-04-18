package org.beijing.presentation.service

import org.beijing.logic.usecases.SearchMealsUseCases
import org.beijing.model.Meal
import org.koin.java.KoinJavaComponent.getKoin

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


