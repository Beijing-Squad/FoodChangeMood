package org.beijing.logic.usecases

import model.GameRound
import org.beijing.logic.MealRepository
import kotlin.random.Random
import org.beijing.model.FeedbackStatus
import org.beijing.model.GuessStatus

class ManageMealsGamesUseCases(
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
            return round.copy(lastFeedBack = FeedbackStatus.ROUND_ALREADY_COMPLETED.message)
        }

        if (round.attemptsLeft <= 0) {
            return round.copy(
                isCompleted = true,
                lastFeedBack = FeedbackStatus.NO_ATTEMPTS_LEFT.message.format(round.meal.minutes)
            )
        }

        val actualMinutes = round.meal.minutes
        val status = when {
            guessedMinutes == actualMinutes -> GuessStatus.CORRECT
            guessedMinutes < actualMinutes -> GuessStatus.TOO_LOW
            else -> GuessStatus.TOO_HIGH
        }
        val feedback = if (status == GuessStatus.CORRECT) {
            status.message.format(actualMinutes)
        } else {
            status.message
        }

        val newAttemptsLeft = round.attemptsLeft - 1
        val isCorrect = guessedMinutes == actualMinutes
        val isCompleted = isCorrect || newAttemptsLeft <= 0

        val finalFeedBack = if (isCompleted && !isCorrect) {
            "$feedback\n"+ FeedbackStatus.GAME_OVER.message.format(actualMinutes)
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