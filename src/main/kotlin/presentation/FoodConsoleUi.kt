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
            9 -> launchGymHelper()

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
        println("9. Gym Helper")

        print("\nhere: ")
    }

    private fun launchGymHelper() {
        print("enter target of Calories: ")
        val targetCalories = readlnOrNull()?.toDoubleOrNull()
        print("enter target of Protein:")
        val targetProtein = readlnOrNull()?.toDoubleOrNull()
        if (targetProtein != null && targetCalories != null) {
            println(
                mealUseCases.getMealsByCaloriesAndProteinUseCases.getMealsByCaloriesAndProtein(
                    targetCalories,
                    targetProtein
                )
            )
        }
    }


    private fun getUserInput(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }

}
