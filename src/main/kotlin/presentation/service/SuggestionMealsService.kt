package org.beijing.presentation.service

import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.model.Meal
import org.beijing.presentation.ViewMealDetails
import presentation.view_read.ConsoleIO

class SuggestionMealsService(
    private val suggestionMeals: ManageMealsSuggestionsUseCase,
    private val viewMealDetails: ViewMealDetails,
    private val consoleIO: ConsoleIO

) : MealService(consoleIO) {

    override fun showOptionService() {
        consoleIO.println("\n\n===Please enter one of the numbers listed below===\n")
        consoleIO.println("1. Suggest a Keto Meal \uD83E\uDD51 ")
        consoleIO.println("2. Suggest Sweets with No Eggs \uD83C\uDF70")
        consoleIO.println("3. Suggest Easy Food \uD83C\uDF2D")
        consoleIO.println("4. Suggest Italian Meals for Large Groups \uD83C\uDF55")
        consoleIO.println("5. Suggest Ten Meals Contains Potato In Ingredients \uD83E\uDD54")
        consoleIO.println("6. Suggest Meal With more than 700 calories \uD83C\uDF54")
        consoleIO.println("0. Exit")
    }

    override fun handleUserChoice() {
        consoleIO.print("\nhere: ")
        when (consoleIO.readInput()) {
            "1" -> launchKetoMealHelper()
            "2" -> launchSweetWithoutEggs()
            "3" -> launchEasyMeals()
            "4" -> launchItalianLargeGroupMeals()
            "5" -> launchTenRandomPotatoMeals()
            "6" -> launchSoThinMeals()
            "0" -> return
            else -> consoleIO.println("❌ Invalid input! Please enter a number between 0 and 6")
        }
    }

    // region Keto Diet
    private fun launchKetoMealHelper() {
        val usedKetoMealIds = mutableSetOf<Int>()
        while (true) {
            val meal = suggestionMeals.suggestKetoMeal(usedKetoMealIds)
            if (meal == null) {
                consoleIO.println("\uD83D\uDE14 No more keto meals to suggest.")
                return
            }

            consoleIO.println("\n🥑 Keto Meal: ${meal.name}")
            consoleIO.println("Short Description: ${meal.description}")

            consoleIO.println("Do you like it? ❤")
            consoleIO.print("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            when (consoleIO.readInput()?.trim()?.lowercase()) {
                "yes" -> {
                    viewMealDetails.displayMealDetails(meal)
                }

                "no" -> {
                    consoleIO.println("🔄 Okay! Let's try another one.")
                    continue
                }

                "exit" -> {
                    break
                }

                else -> {
                    consoleIO.println("⚠️ Please type 'yes' or 'no'")
                }
            }
        }
    }
// endregion

    //region ten random meals contains potato
    fun launchTenRandomPotatoMeals() {
        val tenRandomPotatoMeals = suggestionMeals.suggestTenRandomMealsContainsPotato()

        if (tenRandomPotatoMeals.isEmpty()) {
            consoleIO.println("There is no meals contains potato in their ingredients")
        } else {
            consoleIO.println("-".repeat(70))
            consoleIO.println("\uD83C\uDF55\uD83C\uDF54\uD83C\uDF57List of ten random Meals with potato in their ingredients\uD83C\uDF55\uD83C\uDF54\uD83C\uDF57")
            consoleIO.println("-".repeat(70))
            consoleIO.println(
                "Rank".padEnd(5) + "| " + "Meal Name".padEnd(70)
            )

            tenRandomPotatoMeals.forEachIndexed { index, meal ->
                consoleIO.println(
                    "${index + 1}".padEnd(5) + "| " + meal.name
                )
            }
        }
    }
//endregion

    //region sweets with no eggs
    fun launchSweetWithoutEggs() {
        consoleIO.println("🍬 Welcome to the Egg-Free Sweets Suggester!")
        while (true) {
            val sweet = suggestionMeals.suggestSweetsWithNoEggs()

            if (sweet == null) {
                consoleIO.println("🚫 No more unique sweets without eggs found.")
                break
            }
            consoleIO.println("Try this sweet: ${sweet.name}")
            consoleIO.println("Description: ${sweet.description ?: "No description"}")
            consoleIO.print("Do you like it? (yes to view details / no to see another / exit): ")

            when (consoleIO.readInput()?.lowercase()?.trim()) {
                "yes" -> {
                    viewMealDetails.displayMealDetails(sweet)
                    break
                }

                "no" -> continue
                "exit" -> break
                else -> consoleIO.println("Unknown input.")
            }
        }
    }
//endregion

    // region Italian Large Group Meals
    fun launchItalianLargeGroupMeals() {
        val meals = suggestionMeals.suggestItalianLargeGroupsMeals()
        if (meals.isEmpty()) {
            consoleIO.println("❌ No Italian meals found for large groups.")
        } else {
            consoleIO.println("🍝 Meals from Italy suitable for large groups:\n")
            meals.forEachIndexed { index, meal ->
                consoleIO.println("${index + 1}. ${meal.name} | 🕒 ${meal.minutes} minutes |")
            }
        }
    }
// endregion

    // region easy meal service
    fun launchEasyMeals() {
        consoleIO.println("🥗 Easy Meal Suggestions")
        consoleIO.println("------------------------")
        consoleIO.println("✨ These meals are quick (≤30 mints), simple (≤5 ingredients), and easy (≤6 steps)")
        val meals = suggestionMeals.suggestEasyPreparedMeal()
        if (meals.isEmpty()) {
            consoleIO.println("😔 Sorry, no meals found for '. Try again later!")
        } else {
            consoleIO.println("\n🍽️ Found ${meals.size} meal(s):\n")
            meals.forEachIndexed { index, meal ->
                consoleIO.println("${index + 1}. ${meal.name} • ⏱️ ${meal.minutes} mints • 🧂 ${meal.nIngredients} ingredients • 🔧 ${meal.nSteps} steps")
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

            consoleIO.println("Do You Like This Meal?")
            consoleIO.print("write 'yes' to get details or 'no' to get another meal Or 'exit':")
            consoleIO.print("\nhere: ")
            choice = consoleIO.readInput()?.trim()?.lowercase()
            when (choice) {
                "yes" -> {
                    showMealDetails(meal)
                    break
                }

                "no" -> continue
                "exit" -> return
                else -> {
                    consoleIO.println("Invalid input! Please choose 1, 2 or 0.")
                }
            }
        }
    }

    private fun showMeal(meal: Meal) {
        consoleIO.println("Below Meal With More Than 700 Calories")
        consoleIO.println("Meal Name: ${meal.name}")
        consoleIO.println("Meal Desc: ${meal.description ?: "No Description"}")

    }

    private fun showMealDetails(meal: Meal) {
        consoleIO.println("Below Meal Details:")
        consoleIO.println("Meal ID: ${meal.id}")
        consoleIO.println("Meal Name: ${meal.name}")
        consoleIO.println("Meal Desc: ${meal.description ?: "No Description"}")
        consoleIO.println("Meal Protein: ${meal.nutrition.proteinGrams}")
        consoleIO.println("Meal Sodium: ${meal.nutrition.sodiumGrams}")
        consoleIO.println("Meal Sugar: ${meal.nutrition.sugarGrams}")
        consoleIO.println("Meal Calories: ${meal.nutrition.caloriesKcal}")
        consoleIO.println("Meal TotalFat: ${meal.nutrition.totalFatGrams}")
        consoleIO.println("Meal Carbohydrates: ${meal.nutrition.carbohydratesGrams}")
        consoleIO.println("Meal Saturated: ${meal.nutrition.saturatedFatGrams}")
        consoleIO.println("Meal Tags: ${meal.tags}")
        consoleIO.println("Meal ContributorId: ${meal.contributorId}")
        consoleIO.println("Meal Ingredients:")
        meal.ingredients.forEach { ingredient ->
            consoleIO.println("   • $ingredient")
        }
        consoleIO.println("Meal Steps ((${meal.nSteps} total) :")
        meal.steps.forEachIndexed { index, step ->
            consoleIO.println("   ${index + 1}. $step")
        }
        consoleIO.println("Meal NIngredients: ${meal.nIngredients}")
        consoleIO.println("Meal Minutes: ${meal.minutes}")
        consoleIO.println("Meal Submitted: ${meal.submitted}")

    }

// endregion
}