package org.beijing.presentation

class FoodConsoleUi(
    private val mealUseCases: MealUseCases
) {

    fun start() {
        showWelcome()

        try {
            presentFeatures()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun showWelcome() {
        println("Welcome to Food Change Mood App")
    }


    private fun presentFeatures() {
        showOptions()
        when (val input = getUserInput()) {
            0 -> return
            //write here your feature

            else -> {
                println("Invalid input: $input")
            }
        }

        presentFeatures()
    }

    private fun showOptions() {
        println("\n\n ===Please enter one of the numbers listed below===\n")
        println("0. Exit")
        //write here your feature as string with number

        print("\nhere: ")
    }


    private fun getUserInput(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }

}
