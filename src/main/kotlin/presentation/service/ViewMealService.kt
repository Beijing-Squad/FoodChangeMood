package org.beijing.presentation.service

import org.beijing.logic.usecases.ViewMealsUseCase


fun viewMealService(viewMealsUseCase: ViewMealsUseCase) {

    showViewOptions()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        14 -> showSortedSeaFoodByProtein(viewMealsUseCase)

        0 -> return

        else -> println("Invalid input: $input")
    }
    viewMealService(viewMealsUseCase)

}

fun showViewOptions() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("14. Show Sorted SeaFood By Protein")


    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}


// region Sorted SeaFood
fun showSortedSeaFoodByProtein(viewMealsUseCase: ViewMealsUseCase) {

    println("List Of SeaFood Sorted By Protein:")
    println(String.format("%-6s| %-70s | %-14s", "Rank", "Meal Name", "Protein Content"))
    println("----------------------------------------------------------------------------------------------------")
    viewMealsUseCase.getSortedSeaFoodByProtein().forEachIndexed { index, meal ->
        println(String.format("%-6d| %-70s | %-14d", index + 1, meal.name, meal.nutrition.protein.toInt()))
    }
}
//endregion


