package org.beijing.presentation.service

import org.beijing.logic.usecases.SearchMealsUseCases
import org.beijing.model.Meal

fun searchMealService(searchMealsUseCases: SearchMealsUseCases) {

    showOptionsForSearchMealService()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        1 -> launchGymHelper(searchMealsUseCases)
        2 -> launchIraqiMeals(searchMealsUseCases)


        0 -> return

        else -> println("Invalid input: $input")
    }
    searchMealService(searchMealsUseCases)
}

fun showOptionsForSearchMealService() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Gym Helper")
    println("2. Iraqi Meals")
    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

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

private fun launchIraqiMeals(searchMealsUseCases: SearchMealsUseCases) {
    val iraqiMeals = searchMealsUseCases.getIraqiMeals()
    veiwIraqiMeals(iraqiMeals)
}

fun veiwIraqiMeals(iraqiMeals: List<Meal>) {

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