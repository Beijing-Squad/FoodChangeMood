package org.beijing.presentation.service

import kotlinx.datetime.LocalDate
import org.beijing.logic.usecases.ManageMealsSearchUseCases
import org.beijing.model.Meal
import org.beijing.model.Nutrition
import org.koin.mp.KoinPlatform.getKoin

private val searchMeals: ManageMealsSearchUseCases = getKoin().get()
fun searchMealService() {
    showOptionsForSearchMealService()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        1 -> launchGymHelper()
        2 -> launchSearchByName()
        3 -> launchMealsByDate()
        4 -> searchMealByCountryService()
        0 -> return
        else -> println("Invalid input: $input")
    }
    searchMealService()
}

fun showOptionsForSearchMealService() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Gym Helper")
    println("2. Search by name of meal")
    println("3. Search By Date And See Meal Details")
    println("4. Explore Country Meals")
    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// region search by name
private fun launchSearchByName() {
    val mealNameQuery = readMealNameFromInput()
    val searchResults = searchMeals.getSearchMealsByName(mealNameQuery)
    displaySearchResults(searchResults, mealNameQuery)
}

private fun readMealNameFromInput(): String {
    print("Enter meal name to search: ")
    val input = readlnOrNull()?.trim()
        ?: throw IllegalArgumentException("Meal name input cannot be null.")

    if (input.isEmpty()) {
        throw IllegalArgumentException("Meal name input cannot be empty.")
    }

    return input
}

private fun displaySearchResults(results: List<Meal>, query: String) {
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
            val meal = searchMeals.getMealOnDateById(date, mealId)
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
            println("❌ Invalid Date Format, Please Use (YYYY-MM-DD).")
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
            println("Invalid ID Format, Please Use A Number.")
        }
    }
}

private fun viewMealDetails(meal: Meal) {
    println("╔════════════════════════════════════╗")
    println("║          🍽️ Meal Details           ║")
    println("╚════════════════════════════════════╝")

    println("🟢 Name : ${meal.name}")
    println("⏱️ Preparation Time : ${meal.minutes} minutes")

    println("\n📊 Nutrition Facts:")
    displayNutrition(meal.nutrition)

    println()
    displaySteps(meal.steps)

    println("\n📄 Description:")
    if (meal.description.isNullOrBlank()) {
        println("   No Description Available.")
    } else {
        println("   ${meal.description}")
    }

    println()
    displayIngredients(meal.ingredients)

    println("══════════════════════════════════════")
}

private fun displayNutrition(nutrition: Nutrition) {
    println("   - Calories       : ${nutrition.calories} kcal")
    println("   - Total Fat      : ${nutrition.totalFat} g")
    println("   - Sugar          : ${nutrition.sugar} g")
    println("   - Sodium         : ${nutrition.sodium} mg")
    println("   - Protein        : ${nutrition.protein} g")
    println("   - Saturated Fat  : ${nutrition.saturatedFat} g")
    println("   - Carbohydrates  : ${nutrition.carbohydrates} g")
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
    val targetCalories = readlnOrNull()?.toDoubleOrNull()
    print("enter target of Protein:")
    val targetProtein = readlnOrNull()?.toDoubleOrNull()
    if (targetProtein != null && targetCalories != null) {
        println(
            searchMeals.getGymHelperMeals(
                targetCalories,
                targetProtein
            )
        )
    }
}
//endregion

// region search meal by country
fun searchMealByCountryService() {
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
                val meals = searchMeals.searchMealByCountry(country)
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
