package org.beijing.presentation.service

import kotlinx.datetime.LocalDate
import org.beijing.logic.usecases.ManageMealsSearchUseCase
import org.beijing.model.Meal
import org.beijing.presentation.ViewMealDetails
import org.koin.mp.KoinPlatform.getKoin

class SearchMealService() : MealService() {
    private val searchMeals: ManageMealsSearchUseCase = getKoin().get()
    private val viewMealDetails: ViewMealDetails = getKoin().get()

    override fun showOptionService() {
        println("\n\n===Please enter one of the numbers listed below===\n")
        println("1. Gym Helper")
        println("2. Search by name of meal")
        println("3. Search By Date And See Meal Details")
        println("4. Explore Country Meals")
        println("5. Iraqi Meals")
        println("0. Exit")
    }

    override fun handleUserChoice() {
        print("\nhere: ")
        when (getUserInput()) {
            "1" -> launchGymHelper()
            "2" -> launchSearchByName()
            "3" -> launchMealsByDate()
            "4" -> launchSearchByCountry()
            "5" -> launchIraqiMeals()
            "0" -> return
            else -> println("❌ Invalid input! Please enter a number between 0 and 5")
        }
    }

    // region search by name
    private fun launchSearchByName() {
        try {
            val mealNameQuery = getMealNameFromInput()
            val searchResults = searchMeals.getMealByName(mealNameQuery)
            showMealsSearchResult(searchResults, mealNameQuery)
        } catch (e: IllegalArgumentException) {
            println("❌ ${e.message}")
            showService()
        }
    }

    private fun getMealNameFromInput(): String {
        print("Enter meal name to search: ")
        val userInput = readlnOrNull()?.trim()
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
            println("No meals found matching \"$query\".")
        } else {
            println("Meals found:")
            results.forEach { meal ->
                println(meal.name)
            }
        }
    }
    //endregion

    // region search by add date and see details by id
    private fun launchMealsByDate() {
        val date = getDateInput()
        val mealsOnDate = try {
            searchMeals.getMealsByDate(date)
        } catch (exception: Exception) {
            println(exception.message)
            return
        }
        viewMealsOnDate(mealsOnDate)
        seeMealDetailsById(mealsOnDate)
    }

    private fun getDateInput(): LocalDate {
        while (true) {
            println("Please Enter The Date In Format YYYY-MM-DD")
            print("Enter Date (YYYY-MM-DD): ")
            val input = readln().trim()
            try {
                return LocalDate.parse(input)
            } catch (e: Exception) {
                println("❌ Invalid Date Format, Please Use (YYYY-MM-DD).")
            }
        }
    }

    private fun viewMealsOnDate(meals: List<Meal>) {
        println("=== Meals On [${meals[0].submitted}] ===")
        meals.forEach { meal ->
            println("- ID: ${meal.id}, Name: ${meal.name}")
        }
        println("========================================")
    }

    private fun seeMealDetailsById(mealsOnDate: List<Meal>) {
        val wantsToSeeDetails = getSeeDetailsAnswer()
        if (wantsToSeeDetails) {
            val id = getIdInput()
            try {
                val meal = searchMeals.getMealById(id)
                    .takeIf { foundMeal ->
                        foundMeal in mealsOnDate
                    }
                    ?: throw Exception("❌ Meal with ID [$id] Not Found In The Meals List.")

                viewMealDetails.displayMealDetails(meal)

            } catch (exception: Exception) {
                println(exception.message)
            }
        } else {
            println("Exiting...")
        }
    }

    private fun getSeeDetailsAnswer(): Boolean {
        println("Do You Want To See Details Of A Specific Meal? (yes/no)")
        print("Enter Your Answer: ")
        val answer = readln().trim().lowercase()
        return answer[0] == 'y' || answer[0] == '1'
    }

    private fun getIdInput(): Int {
        while (true) {
            println("Please Enter The Meal ID")
            print("Enter Meal ID: ")
            val input = readln().trim()
            try {
                return input.toInt()
            } catch (e: Exception) {
                println("Invalid ID Format, Please Use A Number.")
            }
        }
    }
    // endregion

    // region gym helper
    private fun launchGymHelper() {
        print("enter target of Calories: ")
        val targetCalories = readlnOrNull()?.trim()?.toDoubleOrNull()
        print("enter target of Protein:")
        val targetProtein = readlnOrNull()?.trim()?.toDoubleOrNull()
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

        println("🍽️🍴 GYM HELPER MEAL PLAN 🍴🍽️")
        println("=".repeat(60))

        meals.forEachIndexed { indexOfMeal, currentMeal ->
            println("\n🔹 Meal ${indexOfMeal + 1}: ${currentMeal.name.uppercase()}")
            println("-".repeat(60))

            println("🕒 Duration: ${currentMeal.minutes} minutes")

            println("\n🥗 Nutrition Info:")
            with(currentMeal.nutrition) {
                println("\t⚡ Calories: $caloriesKcal kcal")
                println("\t💪 Protein: $proteinGrams g")
            }

            println("\n🛒 Ingredients:")
            currentMeal.ingredients.forEachIndexed { index, ingredient ->
                println("\t${index + 1}. $ingredient")
            }

            println("\n👨‍🍳 Preparation Steps:")
            currentMeal.steps.forEachIndexed { index, step ->
                println("\t${index + 1}. $step")
            }

            println("=".repeat(60))
        }

        println("\n✅ All meals displayed successfully!")
    }

    private fun searchAgainAboutGymHelper(meals: List<Meal>) {
        if (meals.isEmpty()) {
            println("\n⚠️ No meals found!\n🍽️ Try searching again or check your filters.\n")
            println("Do you want search again?")
            println("\t1- Yes")
            println("\t0- No")

            print("\nhere: ")
            when (getUserInput()) {
                "1" -> launchGymHelper()
                "0" -> return
                else -> println("Invalid input")
            }
        }
    }
    //endregion

    // region search meal by country
    fun launchSearchByCountry() {
        println("🎌 Welcome to 'Explore Other Countries' Food Culture'!")
        println("------------------------------------------------------")
        println("🍱 In this mini-game, you enter a country name and discover up to 20 random meals from that region.")
        println("🌍 For example, try entering 'Italy', 'India', or 'Mexico'.")

        while (true) {
            println("\n🔎 Enter a country name (or type 'exit' to quit):")
            val country = readlnOrNull()?.trim()

            when {
                country.equals("exit", ignoreCase = true) -> {
                    println("👋 Thanks for playing! Come back soon!")
                    break
                }

                country.isNullOrBlank() || country.length < 4 -> {
                    println("⚠️ Please enter a country name with at least 4 characters.")
                    continue
                }

                country.all { it.isDigit() } -> {
                    println("🚫 Please enter a valid name, not just numbers.")
                    continue
                }

                else -> {
                    val meals = searchMeals.getMealByCountry(country)
                    if (meals.isEmpty()) {
                        println("😔 Sorry, no meals found for '$country'. Try another country!")
                    } else {
                        println("\n🍽️ Found ${meals.size} meal(s) related to '$country':\n")
                        meals.forEachIndexed { index, meal ->
                            println("${index + 1}. ${meal.name} • ⏱️ ${meal.minutes} mins • 🧂 ${meal.nIngredients} ingredients • 🔧 ${meal.nSteps} steps")
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
            println("No Iraqi meals found in the dataset.")
            return
        }

        println("\n===== Iraqi Meals =====")
        println("Found ${iraqiMeals.size} Iraqi meals:")
        iraqiMeals.forEachIndexed { index, meal ->
            println("${index + 1}. ${meal.name}")
        }
    }
    // endregion

}