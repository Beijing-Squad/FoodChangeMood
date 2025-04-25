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
        consoleIO.viewWithLine("\n\n===Please enter one of the numbers listed below===\n")
        consoleIO.viewWithLine("1. Suggest a Keto Meal \uD83E\uDD51 ")
        consoleIO.viewWithLine("2. Suggest Sweets with No Eggs \uD83C\uDF70")
        consoleIO.viewWithLine("3. Suggest Easy Food \uD83C\uDF2D")
        consoleIO.viewWithLine("4. Suggest Italian Meals for Large Groups \uD83C\uDF55")
        consoleIO.viewWithLine("5. Suggest Ten Meals Contains Potato In Ingredients \uD83E\uDD54")
        consoleIO.viewWithLine("6. Suggest Meal With more than 700 calories \uD83C\uDF54")
        consoleIO.viewWithLine("0. Exit")
    }

    override fun handleUserChoice() {
        consoleIO.view("\nhere: ")
        when (consoleIO.readInput()) {
            "1" -> launchKetoMealHelper()
            "2" -> launchSweetWithoutEggs()
            "3" -> launchEasyMeals()
            "4" -> launchItalianLargeGroupMeals()
            "5" -> launchTenRandomPotatoMeals()
            "6" -> launchSoThinMeals()
            "0" -> return
            else -> consoleIO.viewWithLine("❌ Invalid input! Please enter a number between 0 and 6")
        }
    }

    // region Keto Diet
    fun launchKetoMealHelper() {
        val usedKetoMealIds = mutableSetOf<Int>()
        while (true) {
            val meal = suggestionMeals.suggestKetoMeal(usedKetoMealIds)
            if (meal == null) {
                consoleIO.viewWithLine("\uD83D\uDE14 No more keto meals to suggest.")
                return
            }

            consoleIO.viewWithLine("\n🥑 Keto Meal: ${meal.name}")
            consoleIO.viewWithLine("Short Description: ${meal.description}")

            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            when (consoleIO.readInput()?.trim()?.lowercase()) {
                "yes" -> {
                    viewMealDetails.displayMealDetails(meal)
                    break
                }

                "no" -> {
                    consoleIO.viewWithLine("🔄 Okay! Let's try another one.")
                    continue
                }

                "exit" -> {
                    break
                }

                else -> {
                    consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")
                }
            }
        }
    }
// endregion

    //region ten random meals contains potato
    fun launchTenRandomPotatoMeals() {
        val tenRandomPotatoMeals = try {
            suggestionMeals.suggestTenRandomMealsContainsPotato()
        } catch (e: IllegalArgumentException) {
            consoleIO.viewWithLine("${e.message}")
            return
        } catch (e: Exception) {
            consoleIO.viewWithLine("${e.message}")
            return
        }

        consoleIO.viewWithLine("\uD83C\uDF55\uD83C\uDF54\uD83C\uDF57List of ten random Meals with potato in their ingredients\uD83C\uDF55\uD83C\uDF54\uD83C\uDF57")
        consoleIO.viewWithLine("-".repeat(70))
        consoleIO.viewWithLine(
            "Rank".padEnd(5) + "| " + "Meal Name".padEnd(70)
        )
        consoleIO.viewWithLine("-".repeat(70))

        tenRandomPotatoMeals.forEachIndexed { index, meal ->
            consoleIO.viewWithLine(
                "${index + 1}".padEnd(5) + "| " + meal.name
            )
        }
    }
//endregion

    //region sweets with no eggs
    private fun launchSweetWithoutEggs() {
        consoleIO.viewWithLine("🍬 Welcome to the Egg-Free Sweets Suggester!")
        while (true) {
            val sweet = suggestionMeals.suggestSweetsWithNoEggs()

            if (sweet == null) {
                consoleIO.viewWithLine("🚫 No more unique sweets without eggs found.")
                break
            }
            consoleIO.viewWithLine("Try this sweet: ${sweet.name}")
            consoleIO.viewWithLine("Description: ${sweet.description ?: "No description"}")
            consoleIO.view("Do you like it? (yes to view details / no to see another / exit): ")

            when (consoleIO.readInput()!!.lowercase().trim()) {
                "yes" -> {
                    viewMealDetails.displayMealDetails(sweet)
                    break
                }

                "no" -> continue
                "exit" -> { consoleIO.viewWithLine("GoodBye")
                    break
                }
                else -> consoleIO.viewWithLine("Unknown input.")
            }
        }
    }
//endregion

    // region Italian Large Group Meals
    fun launchItalianLargeGroupMeals() {
        val meals = suggestionMeals.suggestItalianLargeGroupsMeals()
        if (meals.isEmpty()) {
            consoleIO.viewWithLine("❌ No Italian meals found for large groups.")
        } else {
            consoleIO.viewWithLine("🍝 Meals from Italy suitable for large groups:\n")
            meals.forEachIndexed { index, meal ->
                consoleIO.viewWithLine("${index + 1}. ${meal.name} | 🕒 ${meal.minutes} minutes |")
            }
        }
    }
// endregion

    // region easy meal service
    fun launchEasyMeals() {
        consoleIO.viewWithLine("🥗 Easy Meal Suggestions")
        consoleIO.viewWithLine("------------------------")
        consoleIO.viewWithLine("✨ These meals are quick (≤30 mints), simple (≤5 ingredients), and easy (≤6 steps)")
        val meals = suggestionMeals.suggestEasyPreparedMeal()
        if (meals.isEmpty()) {
            consoleIO.viewWithLine("😔 Sorry, no meals found for '. Try again later!")
        } else {
            consoleIO.viewWithLine("\n🍽️ Found ${meals.size} meal(s):\n")
            meals.forEachIndexed { index, meal ->
                consoleIO.viewWithLine("${index + 1}. ${meal.name} • ⏱️ ${meal.minutes} mints • 🧂 ${meal.nIngredients} ingredients • 🔧 ${meal.nSteps} steps")
            }
        }
    }
// endregion easy meal service

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

            consoleIO.viewWithLine("Do You Like This Meal?")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal Or 'exit':")
            consoleIO.view("\nhere: ")
            choice = consoleIO.readInput()?.trim()?.lowercase()
            when (choice) {
                "yes" -> {
                    showMealDetails(meal)
                    break
                }

                "no" -> continue
                "exit" -> return
                else -> {
                    consoleIO.viewWithLine("Invalid input! Please choose 1, 2 or 0.")
                }
            }
        }
    }

    private fun showMeal(meal: Meal) {
        consoleIO.viewWithLine("Below Meal With More Than 700 Calories")
        consoleIO.viewWithLine("Meal Name: ${meal.name}")
        consoleIO.viewWithLine("Meal Desc: ${meal.description ?: "No Description"}")

    }

    private fun showMealDetails(meal: Meal) {
        consoleIO.viewWithLine("Below Meal Details:")
        consoleIO.viewWithLine("Meal ID: ${meal.id}")
        consoleIO.viewWithLine("Meal Name: ${meal.name}")
        consoleIO.viewWithLine("Meal Desc: ${meal.description ?: "No Description"}")
        consoleIO.viewWithLine("Meal Protein: ${meal.nutrition.proteinGrams}")
        consoleIO.viewWithLine("Meal Sodium: ${meal.nutrition.sodiumGrams}")
        consoleIO.viewWithLine("Meal Sugar: ${meal.nutrition.sugarGrams}")
        consoleIO.viewWithLine("Meal Calories: ${meal.nutrition.caloriesKcal}")
        consoleIO.viewWithLine("Meal TotalFat: ${meal.nutrition.totalFatGrams}")
        consoleIO.viewWithLine("Meal Carbohydrates: ${meal.nutrition.carbohydratesGrams}")
        consoleIO.viewWithLine("Meal Saturated: ${meal.nutrition.saturatedFatGrams}")
        consoleIO.viewWithLine("Meal Tags: ${meal.tags}")
        consoleIO.viewWithLine("Meal ContributorId: ${meal.contributorId}")
        consoleIO.viewWithLine("Meal Ingredients:")
        meal.ingredients.forEach { ingredient ->
            consoleIO.viewWithLine("   • $ingredient")
        }
        consoleIO.viewWithLine("Meal Steps ((${meal.nSteps} total) :")
        meal.steps.forEachIndexed { index, step ->
            consoleIO.viewWithLine("   ${index + 1}. $step")
        }
        consoleIO.viewWithLine("Meal NIngredients: ${meal.nIngredients}")
        consoleIO.viewWithLine("Meal Minutes: ${meal.minutes}")
        consoleIO.viewWithLine("Meal Submitted: ${meal.submitted}")

    }

// endregion
}