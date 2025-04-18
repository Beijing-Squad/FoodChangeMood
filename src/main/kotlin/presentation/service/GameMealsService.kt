package org.beijing.presentation.service

import model.GameRound
import org.beijing.logic.usecases.ManageMealsGamesUseCases
import org.beijing.model.GameState
import org.koin.mp.KoinPlatform.getKoin

private var currentRound: GameRound? = null
private val gamesMeals: ManageMealsGamesUseCases = getKoin().get()

fun gameMealService() {
    while (true) {
        showGameMealOptions()
        when (val input = getUserInput()) {
            1 -> launchGuessGame()
            0 -> return
            else -> println("Invalid input: $input")
        }
    }
}

fun showGameMealOptions() {
    println("\n\n === Please enter one of the numbers listed below ===")
    println("1. Guess Preparation Time Game")
    println("0. Exit")
}

private fun getUserInput(): Int? = readlnOrNull()?.toIntOrNull()

// region Guess Game Preparation Time
private fun launchGuessGame() {
    currentRound = gamesMeals.startNewRound()
    val mealName = currentRound?.meal?.name ?: "Unknown"

    println("\n🎯 Guess the Preparation Time for: **$mealName** (in minutes)")
    println("You have 3 attempts!")

    while (currentRound != null && currentRound?.isCompleted == false) {
        print("Your guess: ")
        val guess = readlnOrNull()?.toIntOrNull()

        if (guess == null) {
            println("Please enter a valid number.")
            continue
        }

        currentRound = gamesMeals.makeGuess(currentRound!!, guess)
        println(currentRound?.lastFeedBack)
    }

    println("🎮 Game Over! Returning to main menu.\n")
}
// endregion

// region Ingredient Game
private fun launchIngredientGame() {
    println("\uD83D\uDC69\u200D\uD83C\uDF73 Welcome to the Ingredient Game!")
    println("Guess the correct ingredient for each meal. One wrong answer ends the game!")
    var ingredientGameState = GameState()
    var shouldExit = false
    while (!shouldExit && !gamesMeals.isGameOver(ingredientGameState)) {
        val result = gamesMeals.startIngredientGame(ingredientGameState)
        result.fold(
            onSuccess = { (round, updatedState) ->
                ingredientGameState = updatedState
                println("\n🍽 Meal: ${round.mealName}")
                println("Which of the following is one of its ingredients? 🤔")

                round.options.forEachIndexed { index, option ->
                    println("${index + 1}. $option")
                }
                print("Select an option (1-3): ")
                val userChoice = readlnOrNull()?.trim()?.toIntOrNull()

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
                            println("✅ Correct!")
                            println("\uD83C\uDFAF Score: ${ingredientGameState.score}")
                        } else {
                            println("❌ Wrong! The correct answer was: ${round.correctAnswer}")
                            shouldExit = true
                        }
                    },
                    onFailure = { error ->
                        println("⚠ ${error.message}. Game over.")
                        shouldExit = true
                    }
                )
            },
            onFailure = { error ->
                println("⚠ ${error.message}")
                shouldExit = true
            }
        )
    }
    println("\n\uD83C\uDFAF Final Score: ${ingredientGameState.score}")
    if (gamesMeals.isGameOver(ingredientGameState)) {
        println("\uD83C\uDFC6 Congratulations! You win 🎉")
    } else {
        println("👿 Game Over!")
    }
}
// endregion