package org.beijing.logic

import org.beijing.model.GameState
import org.beijing.model.IngredientGameRound
import org.beijing.model.Meal

class IngredientGameUseCase(private val mealRepository: MealRepository) {
    private val meals = mealRepository.getAllMeals()

    companion object {
        private const val MAX_CORRECT_ANSWERS = 15
        private const val SCORE_INCREMENT = 1000
        private const val INCORRECT_OPTION_COUNT = 2
    }

    fun isGameOver(state: GameState): Boolean = state.correctAnswers >= MAX_CORRECT_ANSWERS

    fun playRound(state: GameState): Result<Pair<IngredientGameRound, GameState>> {
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
        val incorrectOptions = meals
            .flatMap { it.ingredients }
            .distinct()
            .filter { it != correct }
            .shuffled()
            .take(INCORRECT_OPTION_COUNT)

        return (incorrectOptions + correct).shuffled()
    }
}
