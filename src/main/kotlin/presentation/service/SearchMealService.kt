package org.beijing.presentation.service

import kotlinx.datetime.LocalDate
import org.beijing.logic.usecases.SearchMealsUseCases
import org.beijing.model.Meal
import org.beijing.model.Nutrition

fun searchMealService() {

}

// region search by add date && see details by id feature (8)
private fun launchMealsByDate(searchMealsUseCases: SearchMealsUseCases) {
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
        println("Please enter the date in format YYYY-MM-DD")
        print("Enter Date (YYYY-MM-DD): ")
        val input = readln()
        try {
            return LocalDate.parse(input)
        } catch (e: Exception) {
            println("Invalid date format. Please use YYYY-MM-DD.")
        }
    }
}

private fun viewMealsOnDate(date: LocalDate, meals: List<Pair<Int, String>>) {
    println("=== Meals on $date ===")
    meals.forEach { meal ->
        println("- ID: ${meal.first}, Name: ${meal.second}")
    }
}

private fun getSeeDetailsAnswer(): Boolean {
    println("Do you want to see details of a specific meal? (yes/no)")
    print("Enter your answer: ")
    val answer = readln().trim().lowercase()
    return answer[0] == 'y'
}

private fun getIdInput(): Int {
    while (true) {
        println("Please enter the meal ID")
        print("Enter Meal ID: ")
        val input = readln()
        try {
            return input.toInt()
        } catch (e: Exception) {
            println("Invalid ID format. Please use a number.")
        }
    }
}

private fun viewMealDetails(meal: Meal) {
    println("==== Meal Details ====")
    println("Name: ${meal.name}")
    println("Minutes: ${meal.minutes}")
    print("Nutrition: ")
    displayNutrition(meal.nutrition)
    println("Steps: ${meal.steps.joinToString(", ")}")
    if (meal.description == null) {
        println("Description: No description available.")
    } else {
        println("Description: ${meal.description}")
    }
    println("Ingredients: ${meal.ingredients.joinToString(", ")}")
}

private fun displayNutrition(nutrition: Nutrition) {
    println(
        "Calories: ${nutrition.calories}, Fat: ${nutrition.totalFat}, Sugar: ${nutrition.sugar}, " +
                "Sodium: ${nutrition.sodium}, Protein: ${nutrition.protein}, Saturated Fat: ${nutrition.saturatedFat}, " +
                "Carbohydrates: ${nutrition.carbohydrates}"
    )
}
// endregion