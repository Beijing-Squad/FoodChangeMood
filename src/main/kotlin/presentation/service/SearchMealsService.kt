package org.beijing.presentation.service

import org.beijing.logic.usecases.SearchMealsUseCases

fun searchMealService(searchMealsUseCases: SearchMealsUseCases){

    showSearchOptions()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        // add number of feature here as ( 1-> featureOne() )

        0 -> return

        else -> println("Invalid input: $input")
    }
    searchMealService(searchMealsUseCases)

}

fun showSearchOptions() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. ") // add feature name here


    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// add ui feature function inside region block

// region Sorted SeaFood


// endregion
