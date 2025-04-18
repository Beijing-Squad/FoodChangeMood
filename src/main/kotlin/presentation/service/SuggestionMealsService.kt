package org.beijing.presentation.service

import org.beijing.logic.usecases.SuggestionMealsUseCases
import org.koin.mp.KoinPlatform.getKoin

private val suggestionMeals: SuggestionMealsUseCases = getKoin().get()
fun suggestionMealService() {

    showSuggestionOptions()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        // add number of feature here as ( 1-> featureOne() )
        1 -> launchItalianLargeGroupMeals()
        2 -> launchTenRandomPotatoMeals()
        0 -> return

        else -> println("Invalid input: $input")
    }
    suggestionMealService()

}

fun showSuggestionOptions() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Suggest Italian Meals for Large Groups") // add feature name here
    println("2. Suggest Ten Meals Contains Potato In Ingredients")


    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// add ui feature function inside region block

// region Sorted SeaFood


// endregion

//region ten random meals contains potato

fun launchTenRandomPotatoMeals() {
    val tenRandomPotatoMeals = suggestionMeals.getTenRandomMealsContainsPotato()

    if (tenRandomPotatoMeals.isEmpty()) {
        println("There is no meals contains potato in their ingredients")
    } else {
        println("-".repeat(70))
        println("\uD83C\uDF55\uD83C\uDF54\uD83C\uDF57List of ten random Meals with potato in their ingredients\uD83C\uDF55\uD83C\uDF54\uD83C\uDF57")
        println("-".repeat(70))
        println(
            "Rank".padEnd(5) + "| " + "Meal Name".padEnd(70)
        )

        tenRandomPotatoMeals.forEachIndexed { index, meal ->
            println(
                "${index + 1}".padEnd(5) + "| " + meal.name
            )
        }
    }
}
//endregion

// region Italian Large Group Meals
fun launchItalianLargeGroupMeals() {
    val meals = suggestionMeals.getItalianLargeGroupsMeals()

    if (meals.isEmpty()) {
        println("❌ No Italian meals found for large groups.")
    } else {
        println("🍝 Meals from Italy suitable for large groups:\n")
        meals.forEachIndexed { index, meal ->
            println("${index + 1}. ${meal.name} | 🕒 ${meal.minutes} minutes |")
        }
    }
}
// endregion