package org.beijing.presentation.service

import org.beijing.logic.usecases.SearchMealsUseCases
import org.beijing.model.Meal

fun searchMealService(searchMealsUseCases: SearchMealsUseCases) {

    showOptions()
    print("\nhere: \n")
    when (val input = getUserInput()) {

        2 -> launchSearchByName(searchMealsUseCases)

        0 -> return

        else -> println("Invalid input: $input")
    }
    searchMealService(searchMealsUseCases)

}

fun showOptions() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Gym Helper")
    println("2. Search by name of meal")


    println("0. Exit")
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


private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}