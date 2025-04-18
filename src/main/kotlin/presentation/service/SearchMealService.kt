package org.beijing.presentation.service

import org.beijing.logic.usecases.SearchMealsUseCases
import org.beijing.model.Meal
import org.koin.mp.KoinPlatform.getKoin

fun searchMealService() {

    showOptionsForSearchMealService()
    print("\nhere: ")
    when (val input = getUserInput()) {
        1 -> launchGymHelper()


        0 -> return

        else -> println("Invalid input: $input")
    }
    searchMealService()
}

fun showOptionsForSearchMealService() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Gym Helper")
    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// region gym helper
private fun launchGymHelper() {
    val searchMeals: SearchMealsUseCases = getKoin().get()
    print("enter target of Calories: ")
    val targetCalories = readlnOrNull()?.toDoubleOrNull()
    print("enter target of Protein: ")
    val targetProtein = readlnOrNull()?.toDoubleOrNull()
    if (targetProtein != null && targetCalories != null) {
        val meals = searchMeals.getGymHelperMeals(
            targetCalories, targetProtein
        )
        showGymHelperResult(meals)

    }
}

private fun showGymHelperResult(meals: List<Meal>) {
    searchAgainAboutGymHelper(meals)
    meals.forEachIndexed { indexOfMeal, currentMeal ->
        println("\n${indexOfMeal + 1}- ${currentMeal.name}")
        println("\t\t\t\t minutes: ${currentMeal.minutes}")
        println("\t\t\t\t nutrition: ")
        currentMeal.nutrition.also { currentNutrition ->
            println("\t\t\t\t\t\t Calories: ${currentNutrition.calories}")
            println("\t\t\t\t\t\t Protein: ${currentNutrition.protein}")
        }
        println("\n\t\t\t\t ingredients: ")
        currentMeal.ingredients.forEachIndexed { indexOfIngredient, currentIngredient ->
            println("\t\t\t\t\t\t ${indexOfIngredient + 1}- $currentIngredient")
        }
        println("\n\t\t\t\t steps: ")
        currentMeal.steps.forEachIndexed { indexOfSteps, currentStep ->
            println("\t\t\t\t\t\t ${indexOfSteps + 1}- $currentStep")
        }

    }
}

private fun searchAgainAboutGymHelper(meals: List<Meal>) {
    if (meals.isEmpty()) {
        println("\nNo meals found\n")
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