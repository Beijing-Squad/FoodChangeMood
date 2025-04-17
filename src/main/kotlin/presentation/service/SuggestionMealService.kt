package org.beijing.presentation.service

import org.beijing.logic.usecases.SuggestionMealsUseCases

fun suggestionMealService(ketoMealUseCase: SuggestionMealsUseCases) {
    showSuggestionsOptions()
    print("\nhere: ")
    when (val input = getUserInput()) {
        1 -> launchKetoMealHelper(ketoMealUseCase)
        0 -> return
        else -> println("Invalid input: $input")
    }
    suggestionMealService(ketoMealUseCase)
}

private fun showSuggestionsOptions() {
    println("\n=== Keto Meal Helper ===")
    println("1. Suggest a Keto Meal 🥑")
    println("0. Return to Main Menu")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

private fun launchKetoMealHelper(ketoMealUseCase: SuggestionMealsUseCases) {
    val usedKetoMealIds = mutableSetOf<Int>()

    while (true) {
        val meal = ketoMealUseCase.suggestKetoMeal(usedKetoMealIds)

        if (meal == null) {
            println("\uD83D\uDE14 No more keto meals to suggest.")
            return
        }

        println("\n🥑 Keto Meal: ${meal.name}")
        println("Short Description: ${meal.description}")

        print("Do you like it?❤ (y = see full details, n = suggest another): ")
        when (readlnOrNull()?.lowercase()) {
            "y" -> {
                println("\uD83D\uDCCC Full Nutritional Info:")
                println("Carbohydrates: ${meal.nutrition.carbohydrates}")
                println("Fat: ${meal.nutrition.totalFat}")
                println("Protein: ${meal.nutrition.protein}")
                break
            }

            "n" -> {
                continue
            }

            else -> {
                println("Invalid input. Please enter y or n")
                break
            }
        }
    }
}
