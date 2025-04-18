package org.beijing.presentation.service

import org.beijing.logic.usecases.SuggestionMealsUseCases
import org.beijing.model.Meal
import org.koin.java.KoinJavaComponent.getKoin
fun suggestionMealService(){
    val suggestionMealsUseCases: SuggestionMealsUseCases=getKoin().get()

    showSuggestionOptions()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        // add number of feature here as ( 1-> featureOne() )
        1 -> launchItalianLargeGroupMeals(suggestionMealsUseCases)
        0 -> return

        else -> println("Invalid input: $input")
    }
    suggestionMealService()

}

fun showSuggestionOptions() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Suggest Italian Meals for Large Groups") // add feature name here


    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// add ui feature function inside region block

// region Sorted SeaFood


// endregion
// region Italian Large Group Meals
fun launchItalianLargeGroupMeals(suggestionMealsUseCases: SuggestionMealsUseCases) {
    val meals = suggestionMealsUseCases.getItalianLargeGroupsMeals()

    if (meals.isEmpty()) {
        println("❌ No Italian meals found for large groups.")
    } else {
        println("🍝 Meals from Italy suitable for large groups:\n")
        meals.forEachIndexed { index, meal ->
            println("${index + 1}. ${meal.name} | 🕒 ${meal.minutes} mins |")
        }
    }
}
// endregion