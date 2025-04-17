package org.beijing.presentation.service

import org.beijing.logic.usecases.ViewMealsUseCases


fun viewMealsService(viewMealsUseCases: ViewMealsUseCases) {

    showOptionsForViewMealsService()
    print("\nhere: \n")
    when (val input = getUserInput()) {
        // add number of feature here as ( 1-> featureOne() )

        0 -> return

        else -> println("Invalid input: $input")
    }
    viewMealsService(viewMealsUseCases)

}

fun showOptionsForViewMealsService() {
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