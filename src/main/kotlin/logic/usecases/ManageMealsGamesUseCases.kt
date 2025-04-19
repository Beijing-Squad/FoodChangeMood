package org.beijing.logic.usecases

import model.GameRound
import org.beijing.logic.MealRepository
import org.beijing.model.IngredientGameRound
import org.beijing.model.IngredientGameState
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
            "$feedback\n" + FeedbackStatus.GAME_OVER.message.format(actualMinutes)
        } else {
            feedback
        }

        return round.copy(
            attemptsLeft = newAttemptsLeft,
            isCompleted = isCompleted,
            lastFeedBack = finalFeedBack
        )
    }

    fun isGameOver(state: IngredientGameState): Boolean = state.correctAnswers >= MAX_CORRECT_ANSWERS

    fun startIngredientGame(state: IngredientGameState): Result<Pair<IngredientGameRound, IngredientGameState>> {
        val meals = mealRepository.getAllMeals()

        if (isGameOver(state)) return Result.failure(Exception("Game Over"))

        val availableMeals = meals.filter { it.id !in state.usedMeals && it.ingredients.isNotEmpty() }

        val meal = availableMeals.shuffled().firstOrNull()
            ?: return Result.failure(Exception("No meals available 😔"))

        val correct = meal.ingredients.randomOrNull()
            ?: return Result.failure(Exception("No ingredients in meal 😔"))

        val options = generateOptions(correct)
        val updatedState = state.copy(usedMeals = state.usedMeals + meal.id)

        return Result.success(IngredientGameRound(meal.name, correct, options) to updatedState)
    }

    fun checkAnswer(userChoice: Int, round: IngredientGameRound, state: IngredientGameState): Pair<Boolean, IngredientGameState> {
        val isCorrect = round.options.getOrNull(userChoice - 1) == round.correctAnswer

        return if (isCorrect) {
            true to state.copy(
                score = state.score + SCORE_INCREMENT,
                correctAnswers = state.correctAnswers + 1
            )
        } else {
            false to state
        }
    }

    private fun generateOptions(correct: String): List<String> {
        val meals = mealRepository.getAllMeals()
        val incorrectOptions = meals
            .flatMap { it.ingredients }
            .distinct()
            .filter { it != correct }
            .shuffled()
            .take(INCORRECT_OPTION_COUNT)

        return (incorrectOptions + correct).shuffled()
    }

    companion object {
        private const val MAX_CORRECT_ANSWERS = 15
        private const val SCORE_INCREMENT = 1000
        private const val INCORRECT_OPTION_COUNT = 2
    }

}