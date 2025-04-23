package org.beijing.presentation.service

import model.GameRound
import org.beijing.logic.usecases.ManageMealsGamesUseCase
import org.beijing.model.IngredientGameState
import presentation.view_read.ConsoleIO

class GameMealsService(
    private val gamesMeals: ManageMealsGamesUseCase,
    private val consoleIO: ConsoleIO
):MealService(consoleIO) {
    private var currentRound: GameRound? = null

    override fun showOptionService() {
        consoleIO.println("\n\n=== Please enter one of the numbers listed below ===")
        consoleIO.println("1. Guess Preparation Time Game")
        consoleIO.println("2. Guess Ingredient Game")
        consoleIO.println("0. Exit")
    }

    override fun handleUserChoice() {
        while (true) {

            when (consoleIO.readInput()) {
                "1" -> launchGuessGame()
                "2" -> launchIngredientGame()
                "0" -> return
                else -> consoleIO.println("❌ Invalid input! Please enter a number between 0 and 2")
            }
        }
    }

    // region Guess Game Preparation Time
    private fun launchGuessGame() {
        currentRound = gamesMeals.startNewRound()
        val mealName = currentRound?.meal?.name ?: "Unknown"

        consoleIO.println("\n🎯 Guess the Preparation Time for: **$mealName** (in minutes)")
        consoleIO.println("You have 3 attempts!")

        while (currentRound != null && currentRound?.isCompleted == false) {
            consoleIO.print("Your guess: ")
            val guess = consoleIO.readInput()?.trim()?.toIntOrNull()

            if (guess == null) {
                consoleIO.println("Please enter a valid number.")
                continue
            }

            currentRound = gamesMeals.makeGuess(currentRound!!, guess)
            consoleIO.println(currentRound?.lastFeedBack)
        }

        consoleIO.println("🎮 Game Over! Returning to main menu.\n")
    }
// endregion

    // region Ingredient Game
    private fun launchIngredientGame() {
        consoleIO.println("\uD83D\uDC69\u200D\uD83C\uDF73 Welcome to the Ingredient Game!")
        consoleIO.println("Guess the correct ingredient for each meal. One wrong answer ends the game!")
        var ingredientGameState = IngredientGameState()
        var shouldExit = false
        while (!shouldExit && !gamesMeals.isGameOver(ingredientGameState)) {
            val result = gamesMeals.startIngredientGame(ingredientGameState)
            result.fold(
                onSuccess = { (round, updatedState) ->
                    ingredientGameState = updatedState
                    consoleIO.println("\n🍽 Meal: ${round.mealName}")
                    consoleIO.println("Which of the following is one of its ingredients? 🤔")

                    round.options.forEachIndexed { index, option ->
                        consoleIO.println("${index + 1}. $option")
                    }
                    consoleIO.print("Select an option (1-3): ")
                    val userChoice = consoleIO.readInput()?.trim()?.toIntOrNull()

                    val inputResult = if (userChoice != null && userChoice in 1..3) {
                        Result.success(userChoice)
                    } else {
                        Result.failure(Exception("Invalid input: Please enter a number between 1 and 3"))
                    }
                    inputResult.fold(
                        onSuccess = { choice ->
                            val (isCorrect, newState) = gamesMeals.checkAnswer(choice, round, ingredientGameState)
                            ingredientGameState = newState

                            if (isCorrect) {
                                consoleIO.println("✅ Correct!")
                                consoleIO.println("\uD83C\uDFAF Score: ${ingredientGameState.score}")
                            } else {
                                consoleIO.println("❌ Wrong! The correct answer was: ${round.correctAnswer}")
                                shouldExit = true
                            }
                        },
                        onFailure = { error ->
                            consoleIO.println("⚠ ${error.message}. Game over.")
                            shouldExit = true
                        }
                    )
                },
                onFailure = { error ->
                    consoleIO.println("⚠ ${error.message}")
                    shouldExit = true
                }
            )
        }
        consoleIO.println("\n\uD83C\uDFAF Final Score: ${ingredientGameState.score}")
        if (gamesMeals.isGameOver(ingredientGameState)) {
            consoleIO.println("\uD83C\uDFC6 Congratulations! You win 🎉")
        } else {
            consoleIO.println("👿 Game Over!")
        }
    }
// endregion
}