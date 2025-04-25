package org.beijing.presentation.service

import org.beijing.logic.usecases.ManageMealsSearchUseCase
import org.beijing.logic.usecases.utils.parseDate
import org.beijing.model.Meal
import org.beijing.presentation.ViewMealDetails
import presentation.view_read.ConsoleIO

class SearchMealService(
    private val searchMeals: ManageMealsSearchUseCase,
    private val viewMealDetails: ViewMealDetails,
    private val consoleIO: ConsoleIO
) : MealService(consoleIO) {

    override fun showOptionService() {
        consoleIO.viewWithLine("\n\n===Please enter one of the numbers listed below===\n")
        consoleIO.viewWithLine("1. Gym Helper")
        consoleIO.viewWithLine("2. Search by name of meal")
        consoleIO.viewWithLine("3. Search By Date And See Meal Details")
        consoleIO.viewWithLine("4. Explore Country Meals")
        consoleIO.viewWithLine("5. Iraqi Meals")
        consoleIO.viewWithLine("0. Exit")
    }

    override fun handleUserChoice() {
        consoleIO.view("\nhere: ")
        when (consoleIO.readInput()) {
            "1" -> launchGymHelper()
            "2" -> launchSearchByName()
            "3" -> launchMealsByDate()
            "4" -> launchSearchByCountry()
            "5" -> launchIraqiMeals()
            "0" -> return
            else -> consoleIO.viewWithLine("❌ Invalid input! Please enter a number between 0 and 5")
        }
    }

    // region search by name
    private fun launchSearchByName() {
        try {
            val mealNameQuery = getMealNameFromInput()
            val searchResults = searchMeals.getMealByName(mealNameQuery)
            showMealsSearchResult(searchResults, mealNameQuery)
        } catch (e: IllegalArgumentException) {
            consoleIO.viewWithLine("❌ ${e.message}")
            showService()
        }
    }

    private fun getMealNameFromInput(): String {
        consoleIO.view("Enter meal name to search: ")
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
            consoleIO.viewWithLine("No meals found matching \"$query\".")
        } else {
            consoleIO.viewWithLine("Meals found:")
            results.forEach { meal ->
                consoleIO.viewWithLine(meal.name)
            }
        }
    }
    //endregion

    // region search by add date and see details by id
    fun launchMealsByDate() {
        val date = getValidDate() ?: return
        val mealsOnDate = searchMeals.getMealsByDate(date)

        if (mealsOnDate.isEmpty()) {
            consoleIO.viewWithLine("❌ No Meals Found For The Date [$date].")
            return
        }

        viewMealsOnDate(mealsOnDate)
        seeMealDetailsById(mealsOnDate)
    }

    fun getValidDate(): String {
        while (true) {
            val date = getDateInput()
            try {
                date.parseDate()
                return date
            } catch (e: Exception) {
                consoleIO.viewWithLine("❌ Invalid date format. Please use (YYYY-MM-DD).")
            }
        }
    }


    private fun getDateInput(): String {
        consoleIO.viewWithLine("Please Enter The Date In Format YYYY-MM-DD")
        consoleIO.view("Enter Date (YYYY-MM-DD): ")
        return consoleIO.readInput()?.trim().toString()
    }

    private fun viewMealsOnDate(meals: List<Meal>) {
        consoleIO.viewWithLine("=== Meals On [${meals[0].submitted}] ===")
        meals.forEach { meal ->
            consoleIO.viewWithLine("- ID: ${meal.id}, Name: ${meal.name}")
        }
        consoleIO.viewWithLine("========================================")
    }

    private fun seeMealDetailsById(mealsOnDate: List<Meal>) {
        val wantsToSeeDetails = getSeeDetailsAnswer()
        if (!wantsToSeeDetails) {
            consoleIO.viewWithLine("Exiting...")
            return
        }

        val id = getIdInput()
        val meal = searchMeals.getMealById(id)

        if (meal == null || meal !in mealsOnDate) {
            consoleIO.viewWithLine("❌ Meal with ID [$id] Not Found On That Date.")
            return
        }

        viewMealDetails.displayMealDetails(meal)
    }

    private fun getSeeDetailsAnswer(): Boolean {
        consoleIO.viewWithLine("Do You Want To See Details Of A Specific Meal? (yes/no)")
        consoleIO.view("Enter Your Answer: ")
        val answer = consoleIO.readInput()?.trim()?.lowercase()
        return answer?.get(0) == 'y'
    }

    private fun getIdInput(): Int {
        while (true) {
            consoleIO.viewWithLine("Please Enter The Meal ID")
            consoleIO.view("Enter Meal ID: ")
            val input = consoleIO.readInput()?.trim()
            try {
                return input?.toInt() ?: throw NumberFormatException()
            } catch (e: Exception) {
                consoleIO.viewWithLine("❌ Invalid ID Format, Please Use A Number.")
            }
        }
    }
    // endregion

    // region gym helper
    private fun launchGymHelper() {
        consoleIO.view("enter target of Calories: ")
        val targetCalories = consoleIO.readInput()?.trim()?.toDoubleOrNull()
        consoleIO.view("enter target of Protein: ")
        val targetProtein = consoleIO.readInput()?.trim()?.toDoubleOrNull()
        if (targetProtein != null && targetCalories != null) {
            try {
                val meals = searchMeals.getGymHelperMealsByCaloriesAndProtein(
                    targetCalories, targetProtein
                )
                showGymHelperResult(meals)
            } catch (e: Exception) {
                consoleIO.viewWithLine(e.message)
                launchGymHelper()
            }

        }
    }

    private fun showGymHelperResult(meals: List<Meal>) {
        searchAgainAboutGymHelper(meals)

        consoleIO.viewWithLine("🍽️🍴 GYM HELPER MEAL PLAN 🍴🍽️")
        consoleIO.viewWithLine("=".repeat(60))

        meals.forEachIndexed { indexOfMeal, currentMeal ->
            consoleIO.viewWithLine("\n🔹 Meal ${indexOfMeal + 1}: ${currentMeal.name.uppercase()}")
            consoleIO.viewWithLine("-".repeat(60))

            consoleIO.viewWithLine("🕒 Duration: ${currentMeal.minutes} minutes")

            consoleIO.viewWithLine("\n🥗 Nutrition Info:")
            with(currentMeal.nutrition) {
                consoleIO.viewWithLine("\t⚡ Calories: $caloriesKcal kcal")
                consoleIO.viewWithLine("\t💪 Protein: $proteinGrams g")
            }

            consoleIO.viewWithLine("\n🛒 Ingredients:")
            currentMeal.ingredients.forEachIndexed { index, ingredient ->
                consoleIO.viewWithLine("\t${index + 1}. $ingredient")
            }

            consoleIO.viewWithLine("\n👨‍🍳 Preparation Steps:")
            currentMeal.steps.forEachIndexed { index, step ->
                consoleIO.viewWithLine("\t${index + 1}. $step")
            }

            consoleIO.viewWithLine("=".repeat(60))
        }

        consoleIO.viewWithLine("\n✅ All meals displayed successfully!")
    }

    private fun searchAgainAboutGymHelper(meals: List<Meal>) {
        if (meals.isEmpty()) {
            consoleIO.viewWithLine("\n⚠️ No meals found!\n🍽️ Try searching again or check your filters.\n")
            consoleIO.viewWithLine("Do you want search again?")
            consoleIO.viewWithLine("\t1- Yes")
            consoleIO.viewWithLine("\t0- No")

            consoleIO.view("\nhere: ")
            when (getUserInput()) {
                "1" -> launchGymHelper()
                "0" -> return
                else -> consoleIO.viewWithLine("Invalid input")
            }
        }
    }
    //endregion

    // region search meal by country
    private fun launchSearchByCountry() {
        consoleIO.viewWithLine("🎌 Welcome to 'Explore Other Countries' Food Culture'!")
        consoleIO.viewWithLine("------------------------------------------------------")
        consoleIO.viewWithLine("🍱 In this mini-game, you enter a country name and discover up to 20 random meals from that region.")
        consoleIO.viewWithLine("🌍 For example, try entering 'Italy', 'India', or 'Mexico'.")

        while (true) {
            consoleIO.viewWithLine("\n🔎 Enter a country name (or type 'exit' to quit):")
            val country = consoleIO.readInput()?.trim()

            when {
                country.equals("exit", ignoreCase = true) -> {
                    consoleIO.viewWithLine("👋 Thanks for playing! Come back soon!")
                    break
                }

                country.isNullOrBlank() || country.length < 4 -> {
                    consoleIO.viewWithLine("⚠️ Please enter a country name with at least 4 characters.")
                    continue
                }

                country.all { it.isDigit() } -> {
                    consoleIO.viewWithLine("🚫 Please enter a valid name, not just numbers.")
                    continue
                }

                else -> {
                    val meals = searchMeals.getMealByCountry(country)
                    if (meals.isEmpty()) {
                        consoleIO.viewWithLine("😔 Sorry, no meals found for '$country'. Try another country!")
                    } else {
                        consoleIO.viewWithLine("\n🍽️ Found ${meals.size} meal(s) related to '$country':\n")
                        meals.forEachIndexed { index, meal ->
                            consoleIO.viewWithLine("${index + 1}. ${meal.name} • ⏱️ ${meal.minutes} mins • 🧂 ${meal.nIngredients} ingredients • 🔧 ${meal.nSteps} steps")
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
            consoleIO.viewWithLine("No Iraqi meals found in the dataset.")
            return
        }

        consoleIO.viewWithLine("\n===== Iraqi Meals =====")
        consoleIO.viewWithLine("Found ${iraqiMeals.size} Iraqi meals:")
        iraqiMeals.forEachIndexed { index, meal ->
            consoleIO.viewWithLine("${index + 1}. ${meal.name}")
        }
    }
    // endregion

}