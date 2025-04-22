package org.beijing.presentation.service

import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.model.Meal
import org.koin.mp.KoinPlatform.getKoin

private val suggestionMeals: ManageMealsSuggestionsUseCase = getKoin().get()
fun suggestionMealService() {
    showOptionsForSuggestionMealService()
    print("\nhere: ")
    when (getUserInput()) {
        "1" -> launchKetoMealHelper()
        "2" -> launchSweetWithoutEggs()
        "3" -> launchEasyMeals()
        "4" -> launchItalianLargeGroupMeals()
        "5" -> launchTenRandomPotatoMeals()
        "6" -> launchSoThinMeals()
        "0" -> return
        else -> println("❌ Invalid input! Please enter a number between 0 and 6")
    }
    suggestionMealService()
}

fun showOptionsForSuggestionMealService() {
    println("\n\n===Please enter one of the numbers listed below===\n")
    println("1. Suggest a Keto Meal \uD83E\uDD51 ")
    println("2. Suggest Sweets with No Eggs \uD83C\uDF70")
    println("3. Suggest Easy Food \uD83C\uDF2D")
    println("4. Suggest Italian Meals for Large Groups \uD83C\uDF55")
    println("5. Suggest Ten Meals Contains Potato In Ingredients \uD83E\uDD54")
    println("6. Suggest Meal With more than 700 calories \uD83C\uDF54")
    println("0. Exit")
}

private fun getUserInput(): String? {
    return readlnOrNull()?.trim()
}

// region Keto Diet
private fun launchKetoMealHelper() {
    val usedKetoMealIds = mutableSetOf<Int>()
    while (true) {
        val meal = suggestionMeals.suggestKetoMeal(usedKetoMealIds)
        if (meal == null) {
            println("\uD83D\uDE14 No more keto meals to suggest.")
            return
        }

        println("\n🥑 Keto Meal: ${meal.name}")
        println("Short Description: ${meal.description}")

        println("Do you like it? ❤")
        print("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
        when (readlnOrNull()?.trim()?.lowercase()) {
            "yes" -> {
                //this function is not working properly, need to fix it later.
//              viewMealDetails(meal)
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

//region ten random meals contains potato
fun launchTenRandomPotatoMeals() {
    val tenRandomPotatoMeals = suggestionMeals.suggestTenRandomMealsContainsPotato()

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

//region sweets with no eggs
fun launchSweetWithoutEggs() {
    println("🍬 Welcome to the Egg-Free Sweets Suggester!")
    while (true) {
        val sweet = suggestionMeals.suggestSweetsWithNoEggs()

        if (sweet == null) {
            println("🚫 No more unique sweets without eggs found.")
            break
        }
        println("Try this sweet: ${sweet.name}")
        println("Description: ${sweet.description ?: "No description"}")
        print("Do you like it? (yes to view details / no to see another / exit): ")

        when (readlnOrNull()?.lowercase()) {
            "yes" -> {
                //this function is not working properly, need to fix it later.
               //viewMealDetails(sweet)
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
    val meals = suggestionMeals.suggestItalianLargeGroupsMeals()
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
fun launchEasyMeals() {
    println("🥗 Easy Meal Suggestions")
    println("------------------------")
    println("✨ These meals are quick (≤30 mints), simple (≤5 ingredients), and easy (≤6 steps)")
    val meals = suggestionMeals.suggestEasyPreparedMeal()
    if (meals.isEmpty()) {
        println("😔 Sorry, no meals found for '. Try again later!")
    } else {
        println("\n🍽️ Found ${meals.size} meal(s):\n")
        meals.forEachIndexed { index, meal ->
            println("${index + 1}. ${meal.name} • ⏱️ ${meal.minutes} mints • 🧂 ${meal.nIngredients} ingredients • 🔧 ${meal.nSteps} steps")
        }
    }
}
// end region easy meal service

// region Suggest Meal With more than 700 calories
fun launchSoThinMeals() {
    val suggestedMeals = mutableListOf<Meal>()
    var meal = suggestionMeals.suggestMealHaveMoreThanSevenHundredCalories().random()
    var choice: String?

    while (true) {

        if (suggestedMeals.contains(meal)) {
            meal = suggestionMeals.suggestMealHaveMoreThanSevenHundredCalories().random()
        }
        suggestedMeals.add(meal)
        showMeal(meal)

        println("Do You Like This Meal?")
        print("write 'yes' to get details or 'no' to get another meal Or 'exit':")
        print("\nhere: ")
        choice = readlnOrNull()?.trim()?.lowercase()
        when (choice) {
            "yes" -> {
                showMealDetails(meal)
                break
            }

            "no" -> continue
            "exit" -> return
            else -> {
                println("Invalid input! Please choose 1, 2 or 0.")
            }
        }
    }
}

private fun showMeal(meal: Meal) {
    println("Below Meal With More Than 700 Calories")
    println("Meal Name: ${meal.name}")
    println("Meal Desc: ${meal.description ?: "No Description"}")

}

private fun showMealDetails(meal: Meal) {
    println("Below Meal Details:")
    println("Meal ID: ${meal.id}")
    println("Meal Name: ${meal.name}")
    println("Meal Desc: ${meal.description ?: "No Description"}")
    println("Meal Protein: ${meal.nutrition.protein}")
    println("Meal Sodium: ${meal.nutrition.sodium}")
    println("Meal Sugar: ${meal.nutrition.sugar}")
    println("Meal Calories: ${meal.nutrition.calories}")
    println("Meal TotalFat: ${meal.nutrition.totalFat}")
    println("Meal Carbohydrates: ${meal.nutrition.carbohydrates}")
    println("Meal Saturated: ${meal.nutrition.saturatedFat}")
    println("Meal Tags: ${meal.tags}")
    println("Meal ContributorId: ${meal.contributorId}")
    println("Meal Ingredients:")
    meal.ingredients.forEach { ingredient ->
        println("   • $ingredient")
    }
    println("Meal Steps ((${meal.nSteps} total) :")
    meal.steps.forEachIndexed { index, step ->
        println("   ${index + 1}. $step")
    }
    println("Meal NIngredients: ${meal.nIngredients}")
    println("Meal Minutes: ${meal.minutes}")
    println("Meal Submitted: ${meal.submitted}")

}

// endregion
