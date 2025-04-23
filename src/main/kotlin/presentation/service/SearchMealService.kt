package org.beijing.presentation.service

import kotlinx.datetime.LocalDate
import org.beijing.logic.usecases.ManageMealsSearchUseCase
import org.beijing.model.Meal
import org.beijing.presentation.ViewMealDetails
import presentation.view_read.ConsoleIO

class SearchMealService(
    private val searchMeals: ManageMealsSearchUseCase,
    private val viewMealDetails: ViewMealDetails,
    private val consoleIO: ConsoleIO
) : MealService(consoleIO) {

    override fun showOptionService() {
        consoleIO.println("\n\n===Please enter one of the numbers listed below===\n")
        consoleIO.println("1. Gym Helper")
        consoleIO.println("2. Search by name of meal")
        consoleIO.println("3. Search By Date And See Meal Details")
        consoleIO.println("4. Explore Country Meals")
        consoleIO.println("5. Iraqi Meals")
        consoleIO.println("0. Exit")
    }

    override fun handleUserChoice() {
        consoleIO.print("\nhere: ")
        when (consoleIO.readInput()) {
            "1" -> launchGymHelper()
            "2" -> launchSearchByName()
            "3" -> launchMealsByDate()
            "4" -> launchSearchByCountry()
            "5" -> launchIraqiMeals()
            "0" -> return
            else -> consoleIO.println("❌ Invalid input! Please enter a number between 0 and 5")
        }
    }

    // region search by name
    private fun launchSearchByName() {
        try {
            val mealNameQuery = getMealNameFromInput()
            val searchResults = searchMeals.getMealByName(mealNameQuery)
            showMealsSearchResult(searchResults, mealNameQuery)
        } catch (e: IllegalArgumentException) {
            consoleIO.println("❌ ${e.message}")
            showService()
        }
    }

    private fun getMealNameFromInput(): String {
        consoleIO.print("Enter meal name to search: ")
        val userInput = consoleIO.readInput()?.trim()
            ?: throw IllegalArgumentException("Meal name input cannot be null.")

        if (userInput.isEmpty()) {
            throw IllegalArgumentException("Meal name input cannot be empty.")
        }

        val isOnlyLettersAndSpaces = userInput.matches(Regex("^[A-Za-z ]+$"))
        if (!isOnlyLettersAndSpaces) {
            throw IllegalArgumentException("Meal name must contain only letters and spaces.")
        }

        return userInput
    }

    private fun showMealsSearchResult(results: List<Meal>, query: String) {
        if (results.isEmpty()) {
            consoleIO.println("No meals found matching \"$query\".")
        } else {
            consoleIO.println("Meals found:")
            results.forEach { meal ->
                consoleIO.println(meal.name)
            }
        }
    }
//endregion

    // region search by add date && see details by id feature (8)
    private fun launchMealsByDate() {
        val date = getDateInput()
        val meals = try {
            searchMeals.getMealsByDate(date)
        } catch (exception: Exception) {
            consoleIO.println(exception.message)
            return
        }

        viewMealsOnDate(date, meals)

        val seeDetailsAnswer = getSeeDetailsAnswer()
        if (seeDetailsAnswer) {
            val mealId = getIdInput()
            try {
                val meal = searchMeals.getMealByDateAndId(date, mealId)
                viewMealDetails.displayMealDetails(meal)
            } catch (exception: Exception) {
                consoleIO.println(exception.message)
                return
            }
        } else {
            consoleIO.println("Exiting...")
        }
    }

    private fun getDateInput(): LocalDate {
        while (true) {
            consoleIO.println("Please Enter The Date In Format YYYY-MM-DD")
            consoleIO.print("Enter Date (YYYY-MM-DD): ")
            val input = consoleIO.readInput()
            try {
                return LocalDate.parse(input.toString())
            } catch (e: Exception) {
                consoleIO.println("❌ Invalid Date Format, Please Use (YYYY-MM-DD).")
            }
        }
    }

    private fun viewMealsOnDate(date: LocalDate, meals: List<Pair<Int, String>>) {
        consoleIO.println("=== Meals On [$date] ===")
        meals.forEach { meal ->
            consoleIO.println("- ID: ${meal.first}, Name: ${meal.second}")
        }
    }

    private fun getSeeDetailsAnswer(): Boolean {
        consoleIO.println("Do You Want To See Details Of A Specific Meal? (yes/no)")
        consoleIO.print("Enter Your Answer: ")
        val answer = consoleIO.readInput()?.trim()?.lowercase()
        return answer?.get(0) == 'y'
    }

    private fun getIdInput(): Int {
        while (true) {
            consoleIO.println("Please Enter The Meal ID")
            consoleIO.print("Enter Meal ID: ")
            val input = consoleIO.readInput()
            try {
                if (input != null) {
                    return input.toInt()
                }
            } catch (e: Exception) {
                consoleIO.println("Invalid ID Format, Please Use A Number.")
            }
        }
    }

// endregion

    // region gym helper
    private fun launchGymHelper() {
        consoleIO.print("enter target of Calories: ")
        val targetCalories = consoleIO.readInput()?.trim()?.toDoubleOrNull()
        consoleIO.print("enter target of Protein:")
        val targetProtein = consoleIO.readInput()?.trim()?.toDoubleOrNull()
        if (targetProtein != null && targetCalories != null) {
            checkIfTargetCaloriesAndTargetProteinAreInvalid(targetCalories, targetProtein)
            val meals = searchMeals.getGymHelperMealsByCaloriesAndProtein(
                targetCalories, targetProtein
            )
            showGymHelperResult(meals)

        }
    }

    private fun checkIfTargetCaloriesAndTargetProteinAreInvalid(targetCalories: Double, targetProtein: Double) {
        if (targetCalories <= 0 || targetProtein <= 0) throw Exception(
            "\nPlease ensure that both Calories " +
                    "and Protein inputs are positive values."
        )
    }

    private fun showGymHelperResult(meals: List<Meal>) {
        searchAgainAboutGymHelper(meals)

        consoleIO.println("🍽️🍴 GYM HELPER MEAL PLAN 🍴🍽️")
        consoleIO.println("=".repeat(60))

        meals.forEachIndexed { indexOfMeal, currentMeal ->
            consoleIO.println("\n🔹 Meal ${indexOfMeal + 1}: ${currentMeal.name.uppercase()}")
            consoleIO.println("-".repeat(60))

            consoleIO.println("🕒 Duration: ${currentMeal.minutes} minutes")

            consoleIO.println("\n🥗 Nutrition Info:")
            with(currentMeal.nutrition) {
                consoleIO.println("\t⚡ Calories: $caloriesKcal kcal")
                consoleIO.println("\t💪 Protein: $proteinGrams g")
            }

            consoleIO.println("\n🛒 Ingredients:")
            currentMeal.ingredients.forEachIndexed { index, ingredient ->
                consoleIO.println("\t${index + 1}. $ingredient")
            }

            consoleIO.println("\n👨‍🍳 Preparation Steps:")
            currentMeal.steps.forEachIndexed { index, step ->
                consoleIO.println("\t${index + 1}. $step")
            }

            consoleIO.println("=".repeat(60))
        }

        consoleIO.println("\n✅ All meals displayed successfully!")
    }

    private fun searchAgainAboutGymHelper(meals: List<Meal>) {
        if (meals.isEmpty()) {
            consoleIO.println("\n⚠️ No meals found!\n🍽️ Try searching again or check your filters.\n")
            consoleIO.println("Do you want search again?")
            consoleIO.println("\t1- Yes")
            consoleIO.println("\t0- No")

            consoleIO.print("\nhere: ")
            when (getUserInput()) {
                "1" -> launchGymHelper()
                "0" -> return
                else -> consoleIO.println("Invalid input")
            }
        }
    }
//endregion

    // region search meal by country
    fun launchSearchByCountry() {
        consoleIO.println("🎌 Welcome to 'Explore Other Countries' Food Culture'!")
        consoleIO.println("------------------------------------------------------")
        consoleIO.println("🍱 In this mini-game, you enter a country name and discover up to 20 random meals from that region.")
        consoleIO.println("🌍 For example, try entering 'Italy', 'India', or 'Mexico'.")

        while (true) {
            consoleIO.println("\n🔎 Enter a country name (or type 'exit' to quit):")
            val country = readlnOrNull()?.trim()

            when {
                country.equals("exit", ignoreCase = true) -> {
                    consoleIO.println("👋 Thanks for playing! Come back soon!")
                    break
                }

                country.isNullOrBlank() || country.length < 4 -> {
                    consoleIO.println("⚠️ Please enter a country name with at least 4 characters.")
                    continue
                }

                country.all { it.isDigit() } -> {
                    consoleIO.println("🚫 Please enter a valid name, not just numbers.")
                    continue
                }

                else -> {
                    val meals = searchMeals.getMealByCountry(country)
                    if (meals.isEmpty()) {
                        consoleIO.println("😔 Sorry, no meals found for '$country'. Try another country!")
                    } else {
                        consoleIO.println("\n🍽️ Found ${meals.size} meal(s) related to '$country':\n")
                        meals.forEachIndexed { index, meal ->
                            consoleIO.println("${index + 1}. ${meal.name} • ⏱️ ${meal.minutes} mins • 🧂 ${meal.nIngredients} ingredients • 🔧 ${meal.nSteps} steps")
                        }
                    }
                }
            }
        }
    }
// endregion search meal by country

    // region iraqi meals
    private fun launchIraqiMeals() {
        val iraqiMeals = searchMeals.getIraqiMeals()
        viewIraqiMeals(iraqiMeals)
    }

    private fun viewIraqiMeals(iraqiMeals: List<Meal>) {

        if (iraqiMeals.isEmpty()) {
            consoleIO.println("No Iraqi meals found in the dataset.")
            return
        }

        consoleIO.println("\n===== Iraqi Meals =====")
        consoleIO.println("Found ${iraqiMeals.size} Iraqi meals:")
        iraqiMeals.forEachIndexed { index, meal ->
            consoleIO.println("${index + 1}. ${meal.name}")
        }
    }
// endregion

}