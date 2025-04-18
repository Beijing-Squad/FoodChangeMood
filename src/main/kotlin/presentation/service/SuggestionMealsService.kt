package org.beijing.presentation.service

import org.beijing.logic.usecases.SuggestionMealsUseCases
import org.koin.mp.KoinPlatform.getKoin

fun suggestionMealService() {
    val suggestionMealsUseCases: SuggestionMealsUseCases = getKoin().get()
    showSuggestionOptions()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        // add number of feature here as ( 1-> featureOne() )
        1 -> launchKetoMealHelper(suggestionMealsUseCases)
        0 -> return

        else -> println("Invalid input: $input")
    }
    suggestionMealService()

}

fun showSuggestionOptions() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Suggest a Keto Meal \uD83E\uDD51 ")
    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// add ui feature function inside region block
// region Keto Diet
private fun launchKetoMealHelper(suggestionMealsUseCases: SuggestionMealsUseCases) {
    val usedKetoMealIds = mutableSetOf<Int>()

    while (true) {
        val meal = suggestionMealsUseCases.suggestKetoMeal(usedKetoMealIds)

        if (meal == null) {
            println("\uD83D\uDE14 No more keto meals to suggest.")
            return
        }

        println("\n🥑 Keto Meal: ${meal.name}")
        println("Short Description: ${meal.description}")

        println("Do you like it? ❤")
        print("write 'yes' to get details or 'no' to get another meal:")
        when (readlnOrNull()?.lowercase()) {
            "yes" -> {
                println("\n📋 Full Meal Details:")
                println("🍽 Name: ${meal.name}")
                println("🕒 Ready in: ${meal.minutes} minutes")
                println("📅 Submitted on: ${meal.submitted}")
                println("\n🥦 Ingredients (${meal.nIngredients}):")
                meal.ingredients.forEach { println("• $it") }

                println("\n📖 Steps (${meal.nSteps}):")
                meal.steps.forEachIndexed { i, step -> println("${i + 1}. $step") }

                println("\n📊 Nutritional Info (per serving):")
                println("• Calories: ${meal.nutrition.calories}")
                println("• Carbohydrates: ${meal.nutrition.carbohydrates}g")
                println("• Total Fat: ${meal.nutrition.totalFat}g")
                println("• Saturated Fat: ${meal.nutrition.saturatedFat}g")
                println("• Sugar: ${meal.nutrition.sugar}g")
                println("• Protein: ${meal.nutrition.protein}g")
                println("• Sodium: ${meal.nutrition.sodium}mg")
            }

            "no" -> {
                println("🔄 Okay! Let's try another one.")
                continue
            }

            else -> {
                println("⚠️ Please type 'yes' or 'no'")
                break
            }
        }
    }
}
// endregion

// region Sorted SeaFood


// endregion
