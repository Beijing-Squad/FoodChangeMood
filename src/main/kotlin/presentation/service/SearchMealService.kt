package org.beijing.presentation.service

import kotlinx.datetime.LocalDate
import org.beijing.logic.usecases.ManageMealsSearchUseCase
import org.beijing.model.Meal
import org.beijing.model.Nutrition
import org.beijing.presentation.utils.MealServiceError
import org.koin.mp.KoinPlatform.getKoin

private val searchMeals: ManageMealsSearchUseCase = getKoin().get()
fun searchMealService() {
    showOptionsForSearchMealService()
    print("\nhere: ")
    when (val input = getUserInput()) {
        1 -> launchGymHelper()
        2 -> launchSearchByName()
        3 -> launchMealsByDate()
        4 -> launchSearchByCountry()
        5 -> launchIraqiMeals()
        0 -> return
        else -> println("Invalid input: $input")
    }
    searchMealService()
}

fun showOptionsForSearchMealService() {
    println("\n\n===Please enter one of the numbers listed below===\n")
    println("1. Gym Helper")
    println("2. Search by name of meal")
    println("3. Search By Date And See Meal Details")
    println("4. Explore Country Meals")
    println("5. Iraqi Meals")
    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.trim()?.toIntOrNull()
}

// region search by name
private fun launchSearchByName() {
    try {
        val mealNameQuery = getMealNameFromInput()
        val searchResults = searchMeals.getMealByName(mealNameQuery)
        showMealsSearchResult(searchResults, mealNameQuery)
    } catch (e: IllegalArgumentException) {
        println(e.message)
        searchMealService()
    }
}

private fun getMealNameFromInput(): String {
    print("Enter meal name to search: ")
    val userInput = readlnOrNull()?.trim()
        ?: throw IllegalArgumentException(MealServiceError.NULL_MEAL_NAME_INPUT.message)

    if (userInput.isEmpty()) {
        throw IllegalArgumentException(MealServiceError.EMPTY_MEAL_NAME_INPUT.message)
    }

    val isOnlyLettersAndSpaces = userInput.matches(Regex("^[A-Za-z ]+$"))
    if (!isOnlyLettersAndSpaces) {
        throw IllegalArgumentException(MealServiceError.INVALID_MEAL_NAME.message)
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

// region search by add date && see details by id feature (8)
private fun launchMealsByDate() {
    val date = getDateInput()
    val meals = try {
        searchMeals.getMealsByDate(date)
    } catch (exception: Exception) {
        println(exception.message)
        return
    }

    viewMealsOnDate(date, meals)

    val seeDetailsAnswer = getSeeDetailsAnswer()
    if (seeDetailsAnswer) {
        val mealId = getIdInput()
        try {
            val meal = searchMeals.getMealByDateAndId(date, mealId)
            viewMealDetails(meal)
        } catch (exception: Exception) {
            println(exception.message)
            return
        }
    } else {
        println("Exiting...")
    }
}

private fun getDateInput(): LocalDate {
    while (true) {
        println("Please Enter The Date In Format YYYY-MM-DD")
        print("Enter Date (YYYY-MM-DD): ")
        val input = readln()
        try {
            return LocalDate.parse(input)
        } catch (e: Exception) {
            println(MealServiceError.INVALID_DATE_FORMAT.message)
        }
    }
}

private fun viewMealsOnDate(date: LocalDate, meals: List<Pair<Int, String>>) {
    println("=== Meals On [$date] ===")
    meals.forEach { meal ->
        println("- ID: ${meal.first}, Name: ${meal.second}")
    }
}

private fun getSeeDetailsAnswer(): Boolean {
    println("Do You Want To See Details Of A Specific Meal? (yes/no)")
    print("Enter Your Answer: ")
    val answer = readln().trim().lowercase()
    return answer[0] == 'y'
}

private fun getIdInput(): Int {
    while (true) {
        println("Please Enter The Meal ID")
        print("Enter Meal ID: ")
        val input = readln()
        try {
            return input.toInt()
        } catch (e: Exception) {
            println(MealServiceError.INVALID_ID_FORMAT.message)
        }
    }
}

fun viewMealDetails(meal: Meal) {
    println("╔════════════════════════════════════╗")
    println("║          🍽️ Meal Details           ║")
    println("╚════════════════════════════════════╝")
    println("🍽 Name : ${meal.name}")
    println("⏱️ Preparation Time : ${meal.minutes} minutes")
    println("\n📊 Nutrition Facts:")
    displayNutrition(meal.nutrition)
    println()
    displaySteps(meal.steps)
    println("\n📄 Description:")
    if (meal.description.isNullOrBlank()) {
        println("  • No Description Available.")
    } else {
        println("  • ${meal.description}")
    }
    println()
    displayIngredients(meal.ingredients)
    println("══════════════════════════════════════")
}

private fun displayNutrition(nutrition: Nutrition) {
    println("   • Calories       : ${nutrition.caloriesKcal} kcal")
    println("   • Total Fat      : ${nutrition.totalFatGrams} g")
    println("   • Sugar          : ${nutrition.sugarGrams} g")
    println("   • Sodium         : ${nutrition.sodiumGrams} mg")
    println("   • Protein        : ${nutrition.proteinGrams} g")
    println("   • Saturated Fat  : ${nutrition.saturatedFatGrams} g")
    println("   • Carbohydrates  : ${nutrition.carbohydratesGrams} g")
}

private fun displaySteps(steps: List<String>) {
    println("📝 Steps (${steps.size}):")
    steps.forEachIndexed { index, step ->
        println("   ${index + 1}. $step")
    }
}

private fun displayIngredients(ingredients: List<String>) {
    println("🧂 Ingredients (${ingredients.size}):")
    ingredients.forEach { ingredient ->
        println("   - $ingredient")
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
    if (targetCalories <= 0 || targetProtein <= 0)
        throw Exception(MealServiceError.INVALID_TARGETS.message)
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
            1 -> launchGymHelper()
            0 -> return
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
// end region search meal by country

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