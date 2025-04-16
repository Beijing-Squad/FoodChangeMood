package org.beijing.logic

import org.beijing.model.IngredientGameRound
import org.beijing.model.Meal

class IngredientGameUseCase(private val mealRepository: MealRepository) {
    private val meals = mealRepository.getAllMeals()
    private var score = 0
    private var correctAnswers = 0
    private val maxCorrectAnswers = 15
    private val usedMeals = mutableSetOf<String>()

    fun playRound(): Result<IngredientGameRound>? {
        return if (isGameOver()) Result.failure(Exception("Game Over"))
        else {
            getRandomMeal()?.let { meal ->
                getRandomIngredient(meal)?.let { correct ->
                    val options = getOptions(correct)
                    Result.success(IngredientGameRound(meal.name, correct, options))
                }
            } ?: Result.failure(Exception("No meals or ingredients available"))
        }
    }

    fun checkAnswer(userChoice: Int, round: IngredientGameRound): Boolean {
        val isCorrect = round.options.getOrNull(userChoice - 1) == round.correctAnswer
        if (isCorrect) {
            score += 1000
            correctAnswers++
        }
        return isCorrect
    }


    fun isGameOver(): Boolean {
        return correctAnswers >= maxCorrectAnswers
    }

    fun getScore(): Int {
        return score
    }

    private fun getRandomMeal(): Meal? {
        return meals.asSequence()
            .filter { it.name !in usedMeals }
            .filter { it.ingredients.isNotEmpty() }
            .shuffled()
            .firstOrNull()
            ?.also { usedMeals.add(it.name) }
    }

    private fun getRandomIngredient(meal: Meal): String? {
        return meal.ingredients.takeIf { it.isNotEmpty() }?.random()
    }

    private fun getOptions(correct: String): List<String> {
        return (meals
            .flatMap { it.ingredients }
            .distinct()
            .filter { it != correct }
            .shuffled()
            .take(2) + correct)
            .shuffled()
    }
}