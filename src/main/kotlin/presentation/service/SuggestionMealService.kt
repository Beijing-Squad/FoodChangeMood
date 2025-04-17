package org.beijing.presentation.service

import org.beijing.logic.usecases.SuggestionMealsUseCases
import org.beijing.model.Meal



fun suggestionMealService(suggestionMealsUseCases: SuggestionMealsUseCases) {

    showSuggestionOptions()
    print("\nhere: ")
    when (val input = getUserInput()) {
        1 -> suggestMealWithMoreThanSevenHundredCalories(suggestionMealsUseCases)

        0 -> return

        else -> println("Invalid input: $input")
    }
    suggestionMealService(suggestionMealsUseCases)

}

fun showSuggestionOptions() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1.Suggest Meal With More Than 700 Calories ") // add feature name here


    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}


// region Suggest Meal With more than 700 calories

private fun suggestMealWithMoreThanSevenHundredCalories(suggestionMealsUseCases: SuggestionMealsUseCases) {
    val suggestedMeals = mutableListOf<Meal>()
    var meal = suggestionMealsUseCases.suggestMealHaveMoreThanSevenHundredCalories().random()
    var choice: Int?

    while (true) {

        if (suggestedMeals.contains(meal)) {
            meal = suggestionMealsUseCases.suggestMealHaveMoreThanSevenHundredCalories().random()
        }
        suggestedMeals.add(meal)
        showMeal(meal)

        println("Do You Like This Meal?")
        println("1. Yes")
        println("2. No")
        println("0. Exit")
        print("\nhere: ")

        choice = readlnOrNull()?.toIntOrNull()
        when (choice) {
            1 -> {
                showMealDeatils(meal)
                break
            }

            2 -> continue
            0 -> return
            else -> {
                println("Invalid input! Please choose 1, 2 or 0.")
            }
        }
    }


}

private fun showMeal(meal: Meal) {
    println("Below Meal With More Than 700 Calories")
    println("Meal Name: ${meal.name}")
    println("Meal Desc: ${meal.description ?: "No Description"}")

}

private fun showMealDeatils(meal: Meal) {
    println("Below Meal Details:")
    println("Meal ID: ${meal.id}")
    println("Meal Name: ${meal.name}")
    println("Meal Desc: ${meal.description ?: "No Description"}")
    println("Meal Protein: ${meal.nutrition.protein}")
    println("Meal Sodium: ${meal.nutrition.sodium}")
    println("Meal Sugar: ${meal.nutrition.sugar}")
    println("Meal Calories: ${meal.nutrition.calories}")
    println("Meal TotalFat: ${meal.nutrition.totalFat}")
    println("Meal Carbohydrates: ${meal.nutrition.carbohydrates}")
    println("Meal Saturated: ${meal.nutrition.saturatedFat}")
    println("Meal Tags: ${meal.tags}")
    println("Meal ContributorId: ${meal.contributorId}")
    println("Meal Ingredients: ${meal.ingredients}")
    println("Meal NIngredients: ${meal.nIngredients}")
    println("Meal Minutes: ${meal.minutes}")
    println("Meal NSteps: ${meal.nSteps}")
    println("Meal Steps: ${meal.steps}")
    println("Meal Submitted: ${meal.submitted}")

}

// endregion