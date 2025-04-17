package org.beijing.presentation.service

import kotlinx.datetime.LocalDate
import org.beijing.logic.usecases.SearchMealsUseCases
import org.beijing.model.Meal
import org.beijing.model.Nutrition
fun searchMealService(searchMealsUseCases: SearchMealsUseCases) {

    showOptionsForSearchMealService()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        1 -> launchGymHelper(searchMealsUseCases)
        12 -> launchMealsByDate(searchMealsUseCases)
        0 -> return

        else -> println("Invalid input: $input")
    }
    searchMealService(searchMealsUseCases)

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
    println("==== Meal Details ====")
    println("Name: ${meal.name}")
    println("Minutes: ${meal.minutes}")
    print("Nutrition: ")
    displayNutrition(meal.nutrition)
    println("Steps: ${meal.steps.joinToString(", ")}")
    if (meal.description == null) {
        println("Description: No Description Available.")
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

// region gym helper
private fun launchGymHelper(searchMealsUseCases: SearchMealsUseCases) {
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