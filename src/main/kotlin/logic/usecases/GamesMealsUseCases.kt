package org.beijing.logic.usecases

import model.GameRound
import org.beijing.logic.MealRepository
import kotlin.random.Random

class GamesMealsUseCases(
    private val mealRepository: MealRepository,
) {
    private val maxAttempts = 3

    fun startNewRound(): GameRound {
        val meals = mealRepository.getAllMeals()
        if (meals.isEmpty()) {
            throw IllegalArgumentException("No meals found in the repository")
        }

        val randomIndex = Random.nextInt(meals.size)
        val selectedMeal = meals[randomIndex]

        return GameRound(
            meal = selectedMeal,
            attemptsLeft = maxAttempts,
            isCompleted = false,
            lastFeedBack = null
        )
    }

    fun makeGuess(round: GameRound, guessedMinutes: Int): GameRound {
        if (round.isCompleted) {
            return round.copy(lastFeedBack = "This round is already Completed, Start A new Round.")
        }

        if (round.attemptsLeft <= 0) {
            return round.copy(
                isCompleted = true,
                lastFeedBack = "No Attempts Left, The Actual Preparation Time is: ${round.meal.minutes} minutes."
            )
        }

        val actualMinutes = round.meal.minutes
        val feedback = when {
            guessedMinutes == actualMinutes -> {
                "Correct!! The Preparation Time is indeed $actualMinutes minutes."
            }

            guessedMinutes < actualMinutes -> {
                "Too low!! Try a higher number."
            }

            else -> {
                "Too high! Try a lower number."
            }
        }

        val newAttemptsLeft = round.attemptsLeft - 1
        val isCorrect = guessedMinutes == actualMinutes
        val isCompleted = isCorrect || newAttemptsLeft <= 0

        val finalFeedBack = if (isCompleted && !isCorrect) {
            "$feedback\nGameOver! The actual preparation time is $actualMinutes minutes."
        } else {
            feedback
        }

        return round.copy(
            attemptsLeft = newAttemptsLeft,
            isCompleted = isCompleted,
            lastFeedBack = finalFeedBack
        )
    }
}