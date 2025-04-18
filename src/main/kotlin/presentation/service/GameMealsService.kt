package org.beijing.presentation.service

import org.beijing.logic.usecases.GamesMealsUseCases
import org.koin.mp.KoinPlatform.getKoin


private var currentRound: GamesMealsUseCases.GameRound? = null
private val gamesMealsUseCases: GamesMealsUseCases = getKoin().get()

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
    currentRound = gamesMealsUseCases.startNewRound()
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

        currentRound = gamesMealsUseCases.makeGuess(currentRound!!, guess)
        println(currentRound?.lastFeedBack)
    }

    println("🎮 Game Over! Returning to main menu.\n")
}
// endregion
