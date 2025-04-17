package org.beijing.presentation.service

import org.beijing.logic.usecases.SearchMealsUseCases

fun searchMealService(searchMealsUseCases: SearchMealsUseCases) {

    showOptions()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        1 -> launchGymHelper(searchMealsUseCases)

        0 -> return

        else -> println("Invalid input: $input")
    }
    searchMealService(searchMealsUseCases)

}

fun showOptions() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Gym Helper")


    println("0. Exit")
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


private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}
