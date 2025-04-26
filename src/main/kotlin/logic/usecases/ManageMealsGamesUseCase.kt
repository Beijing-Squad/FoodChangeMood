package org.beijing.logic.usecases

import model.GameRound
import org.beijing.logic.MealRepository
import org.beijing.model.IngredientGameRound
import org.beijing.model.IngredientGameState
import kotlin.random.Random

class ManageMealsGamesUseCase(
    private val mealRepository: MealRepository,
) {
    private val meals = mealRepository.getAllMeals()

    // region preparation time guess game
    fun startNewRound(): GameRound {
        if (meals.isEmpty()) {
            throw IllegalArgumentException(NO_MEALS_IN_REPO)
        }

        val randomIndex = Random.nextInt(meals.size)
        val selectedMeal = meals[randomIndex]

        return GameRound(
            meal = selectedMeal,
            attemptsLeft = MAX_ATTEMPTS,
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
    // endregion

    // region ingredient game
    fun startIngredientGame(state: IngredientGameState): Result<Pair<IngredientGameRound, IngredientGameState>> {
        if (isGameOver(state)) return Result.failure(Exception(END_GAME))

        val availableMeal = meals
            .asSequence()
            .filter { it.id !in state.usedMeals && it.ingredients.isNotEmpty() }
            .shuffled()
            .firstOrNull()

        return availableMeal?.let { meal ->
            val correct = meal.ingredients.random()
            val options = generateOptions(correct)
            val updatedState = state.copy(usedMeals = state.usedMeals + meal.id)
            Result.success(IngredientGameRound(meal.name, correct, options) to updatedState)
        } ?: Result.failure(Exception(NO_MEALS))
    }
    fun checkAnswer(
        userChoice: Int,
        round: IngredientGameRound,
        state: IngredientGameState
    ): Pair<Boolean, IngredientGameState> {
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

    fun isGameOver(state: IngredientGameState): Boolean = state.correctAnswers >= MAX_CORRECT_ANSWERS

    companion object {
        private const val MAX_CORRECT_ANSWERS = 15
        private const val SCORE_INCREMENT = 1000
        private const val INCORRECT_OPTION_COUNT = 2
        private const val MAX_ATTEMPTS = 3
        private const val END_GAME = "Game Over"
        private const val NO_MEALS = "No meals available 😔"
        private const val NO_INGREDIENTS = "No ingredients in meal 😔"
        private const val NO_MEALS_IN_REPO = "No meals found in the repository"
    }
    // endregion

    enum class FeedbackStatus(val message: String) {
        NO_ATTEMPTS_LEFT("No Attempts Left, The Actual Preparation Time is: %d minutes."),
        ROUND_ALREADY_COMPLETED("This round is already Completed, Start A new Round."),
        GAME_OVER("GameOver! The actual preparation time is %d minutes."),
    }

    enum class GuessStatus(val message: String) {
        TOO_HIGH("Too high! Try a lower number."),
        TOO_LOW("Too low! Try a higher number."),
        CORRECT("Correct!! The preparation time is indeed %d minutes.")
    }
}