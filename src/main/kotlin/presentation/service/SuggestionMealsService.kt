package org.beijing.presentation.service

import org.beijing.logic.usecases.SuggestionMealsUseCases
import org.koin.mp.KoinPlatform.getKoin

private val suggestionMealsUseCases: SuggestionMealsUseCases = getKoin().get()

fun suggestionMealService() {
    showSuggestionOptions()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        // add number of feature here as ( 1-> featureOne() )
        8 -> launchKetoMealHelper()
        22 -> sweetsWithNoEggsUi()

        1 -> launchItalianLargeGroupMeals()
        0 -> return

        else -> println("Invalid input: $input")
    }
    suggestionMealService()

}

fun showSuggestionOptions() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("8. Suggest a Keto Meal \uD83E\uDD51 ")
    println("22. Sweets with No Eggs") // add feature name here
    println("1. Suggest Italian Meals for Large Groups") // add feature name here


    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// add ui feature function inside region block
// region Keto Diet
private fun launchKetoMealHelper() {
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

            "exit" -> {
                break
            }

            else -> {
                println("⚠️ Please type 'yes' or 'no'")
            }
        }
    }
}
// endregion

// region Sorted SeaFood


// endregion


//region sweets with no eggs
fun sweetsWithNoEggsUi() {
    println("🍬 Welcome to the Egg-Free Sweets Suggester!")

    while (true) {
        val sweet = suggestionMeals.getSweetWithNoEggs()

        if (sweet == null) {
            println("🚫 No more unique sweets without eggs found.")
            break
        }

        println("Try this sweet: ${sweet.name}")
        println("Description: ${sweet.description ?: "No description"}")
        print("Do you like it? (yes to view details / no to see another / exit): ")

        when (readlnOrNull()?.lowercase()) {
            "yes" -> {
                println("\n✅ Name: ${sweet.name}")
                println("🕒 Prep Time: ${sweet.minutes} minutes")

                println("📊 Nutrition:")
                println("   • Calories: ${sweet.nutrition.calories}")
                println("   • Total Fat: ${sweet.nutrition.totalFat}")
                println("   • Sugar: ${sweet.nutrition.sugar}")
                println("   • Sodium: ${sweet.nutrition.sodium}")
                println("   • Protein: ${sweet.nutrition.protein}")
                println("   • Saturated Fat: ${sweet.nutrition.saturatedFat}")
                println("   • Carbohydrates: ${sweet.nutrition.carbohydrates}")

                println("\n🧾 Ingredients:")
                sweet.ingredients.forEach { ingredient ->
                    println("   • $ingredient")
                }

                println("\n🍽 Steps (${sweet.nSteps} total):")
                sweet.steps.forEachIndexed { index, step ->
                    println("   ${index + 1}. $step")
                }

                println()
                break
            }

            "no" -> continue
            "exit" -> break
            else -> println("Unknown input.")
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