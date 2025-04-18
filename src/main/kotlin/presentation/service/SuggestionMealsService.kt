package org.beijing.presentation.service

import org.beijing.logic.usecases.SuggestionMealsUseCases
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.mp.KoinPlatform

fun suggestionMealService(){
    val suggestionMealsUseCases: SuggestionMealsUseCases=getKoin().get()
import org.koin.mp.KoinPlatform.getKoin

private val suggestionMeals: SuggestionMealsUseCases = getKoin().get()
fun suggestionMealService() {

    showSuggestionOptions()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        // add number of feature here as ( 1-> featureOne() )
        22 -> sweetsWithNoEggsUi()

        1 -> launchItalianLargeGroupMeals()
        0 -> return

        else -> println("Invalid input: $input")
    }
    suggestionMealService()

}

fun showSuggestionOptions() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("22. Sweets with No Eggs") // add feature name here
    println("1. Suggest Italian Meals for Large Groups") // add feature name here


    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// add ui feature function inside region block

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

// region easy meal service
fun easyMealService() {
    val suggestionMealsUseCases : SuggestionMealsUseCases = KoinPlatform.getKoin().get()
    println("🥗 Easy Meal Suggestions")
    println("------------------------")
    println("✨ These meals are quick (≤30 mins), simple (≤5 ingredients), and easy (≤6 steps)")
    val meals = suggestionMealsUseCases.easyFoodSuggestion()
    if (meals.isEmpty()) {
        println("😔 Sorry, no meals found for '. Try again later!")
    } else {
        println("\n🍽️ Found ${meals.size} meal(s):\n")
        meals.forEachIndexed { index, meal ->
            println("${index + 1}. ${meal.name} • ⏱️ ${meal.minutes} mins • 🧂 ${meal.nIngredients} ingredients • 🔧 ${meal.nSteps} steps")
        }
    }
}
// end region easy meal service
