package org.beijing.presentation

import org.beijing.logic.IngredientGameUseCase

class FoodConsoleUi(
    private val mealUseCases: MealUseCases,
    private val ingredientGameUseCase: IngredientGameUseCase

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
            13 -> startIngredientGame()
            //write here your feature

            else -> {
                println("Invalid input: $input")
            }
        }

        presentFeatures()
    }

    private fun showOptions() {
        println("\n\n ===Please enter one of the numbers listed below===\n")
        println("13. Start Ingredient Game 🍲")
        println("0. Exit")
        //write here your feature as string with number

        print("\nhere: ")
    }


    private fun getUserInput(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }
    fun startIngredientGame() {
        println("\uD83D\uDC69\u200D\uD83C\uDF73 Welcome to the Ingredient Game!")
        println("Guess the correct ingredient for each meal. One wrong answer ends the game!")

        var shouldExit = false

        while (!shouldExit) {
            val roundResult = ingredientGameUseCase.playRound()

            val round = roundResult?.getOrNull()
            if (round == null) {
                println("⚠️ Error occurred while starting the round.")
                break
            }

            println("\n\uD83C\uDF7D Meal: ${round.mealName} \uD83C\uDF7D")
            println("Which of the following is one of its ingredients?🤔")

            round.options.forEachIndexed { index, option ->
                println("${index + 1}. $option")
            }
            print("Select an option (1-3): ")
            val userChoice = readlnOrNull()?.toIntOrNull()

            val inputResult = if (userChoice != null && userChoice in 1..3) {
                Result.success(userChoice)
            } else {
                Result.failure(Exception(" invalid input: Please enter a number between 1 and 3"))
            }
            inputResult.fold(
                onSuccess = { choice ->
                    val isCorrect = ingredientGameUseCase.checkAnswer(choice, round)
                    if (isCorrect) {
                        println("✅ Correct! Score: ${ingredientGameUseCase.getScore()}")
                    } else {
                        println("❌ Wrong! The correct answer was: ${round.correctAnswer}")
                        shouldExit = true
                    }
                },
                onFailure = { error ->
                    println("⚠️ ${error.message}. Game over.")
                    shouldExit = true
                }
            )

        }

        println("\n\uD83C\uDFAF Final Score: ${ingredientGameUseCase.getScore()}")
        if (ingredientGameUseCase.isGameOver()) {
            println("\uD83C\uDFC6 Congratulations! You win\uD83C\uDF89")
        } else {
            println("\uD83D\uDE08 Game Over!")
        }
    }
}
