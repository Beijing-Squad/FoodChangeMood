package org.beijing.presentation

class FoodConsoleUi(
    //here inject use cases objects
) {

    fun start() {
        showWelcome()
        presentFeatures()
    }

    private fun showWelcome() {
        println("Welcome to Food Change Mood App")
    }


    private fun presentFeatures(startIndex: Int = 0) {
        showOptions()
        val input = getUserInput()

        when (input) {
            0 -> return
            //write here your feature
            else -> {
                println("Invalid input.")
            }
        }

        presentFeatures(startIndex)
    }

    private fun showOptions() {
        println("\n\n ===Please enter one of the numbers listed below===\n")
        //write here your feature as string with number

        print("here: ")
    }


    private fun getUserInput(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }

}