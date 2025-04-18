package org.beijing.logic.usecases

import model.GameRound
import org.beijing.logic.MealRepository
import kotlin.random.Random

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

    data class GameRound(
        val meal: Meal,
        val attemptsLeft: Int,
        val isCompleted: Boolean,
        val lastFeedBack: String?
    )

    fun isGameOver(state: GameState): Boolean = state.correctAnswers >= MAX_CORRECT_ANSWERS

    fun startIngredientGame(state: GameState): Result<Pair<IngredientGameRound, GameState>> {
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

    fun checkAnswer(userChoice: Int, round: IngredientGameRound, state: GameState): Pair<Boolean, GameState> {
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
    data class GameState(
        val score: Int = 0,
        val correctAnswers: Int = 0,
        val usedMeals: Set<Int> = emptySet()
    )

    data class IngredientGameRound(
        val mealName: String,
        val correctAnswer: String,
        val options: List<String>
    )

}