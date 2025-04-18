package org.beijing.presentation.service

import kotlinx.datetime.LocalDate
import org.beijing.logic.usecases.SearchMealsUseCases
import org.beijing.model.Meal
import org.beijing.model.Nutrition
import org.koin.mp.KoinPlatform.getKoin

private val searchMealsUseCases: SearchMealsUseCases = getKoin().get()
fun searchMealService() {

    showOptionsForSearchMealService()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        1 -> launchGymHelper()
        12 -> launchMealsByDate()
        0 -> return

        else -> println("Invalid input: $input")
    }
    searchMealService()
}

fun showOptionsForSearchMealService() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Gym Helper")
    println("12. Search By Date And See Meal Details")

    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// region search by add date && see details by id feature (8)
private fun launchMealsByDate() {
    val date = getDateInput()

    val meals = try {
        searchMealsUseCases.getMealsByDate(date)
    } catch (exception: Exception) {
        println(exception.message)
        return
    }

    viewMealsOnDate(date, meals)

    val seeDetailsAnswer = getSeeDetailsAnswer()
    if (seeDetailsAnswer) {
        val mealId = getIdInput()
        try {
            val meal = searchMealsUseCases.getMealOnDateById(date, mealId)
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
            println("Invalid Date Format, Please Use (YYYY-MM-DD).")
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
            searchMealsUseCases.getGymHelperMeals(
                targetCalories,
                targetProtein
            )
        )
    }
}
//endregion