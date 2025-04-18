package org.beijing.presentation.service

import org.beijing.logic.usecases.ManageMealsSuggestionsUseCases
import org.beijing.logic.usecases.SuggestionMealsUseCases
import org.beijing.model.Meal
import org.koin.mp.KoinPlatform.getKoin

private val suggestionMeals: ManageMealsSuggestionsUseCases = getKoin().get()
fun suggestionMealService() {
    showSuggestionOptions()
    print("\nhere: ")
    when (val input = getUserInput()) {
        1 -> launchKetoMealHelper()
        2 -> sweetsWithNoEggsUi()
        3 -> launchEasyMeal()
        4 -> launchItalianLargeGroupMeals()
        5 -> launchTenRandomPotatoMeals()
        0 -> return
        else -> println("Invalid input: $input")
    }
    suggestionMealService()
}

fun showSuggestionOptions() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Suggest a Keto Meal \uD83E\uDD51 ")
    println("2. Sweets with No Eggs")
    println("3. Easy Food Suggestion")
    println("4. Suggest Italian Meals for Large Groups")
    println("5. Suggest Ten Meals Contains Potato In Ingredients")
    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
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
        print("write 'yes' to get details or 'no' to get another meal:")
        when (readlnOrNull()?.trim()?.lowercase()){
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
fun launchEasyMeal() {
    println("🥗 Easy Meal Suggestions")
    println("------------------------")
    println("✨ These meals are quick (≤30 mints), simple (≤5 ingredients), and easy (≤6 steps)")
    val meals = suggestionMeals.getEasyFoodSuggestion()
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

private fun suggestMealWithMoreThanSevenHundredCalories(suggestionMealsUseCases: SuggestionMealsUseCases) {
    val suggestedMeals = mutableListOf<Meal>()
    var meal = suggestionMealsUseCases.suggestMealHaveMoreThanSevenHundredCalories().random()
    var choice: Int?

    while (true) {

        if (suggestedMeals.contains(meal)) {
            meal = suggestionMealsUseCases.suggestMealHaveMoreThanSevenHundredCalories().random()
        }
        suggestedMeals.add(meal)
        showMeal(meal)

        println("Do You Like This Meal?")
        println("1. Yes")
        println("2. No")
        println("0. Exit")
        print("\nhere: ")

        choice = readlnOrNull()?.toIntOrNull()
        when (choice) {
            1 -> {
                showMealDeatils(meal)
                break
            }

            2 -> continue
            0 -> return
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

private fun showMealDeatils(meal: Meal) {
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
    println("Meal Ingredients: ${meal.ingredients}")
    println("Meal NIngredients: ${meal.nIngredients}")
    println("Meal Minutes: ${meal.minutes}")
    println("Meal NSteps: ${meal.nSteps}")
    println("Meal Steps: ${meal.steps}")
    println("Meal Submitted: ${meal.submitted}")

}

// endregion
