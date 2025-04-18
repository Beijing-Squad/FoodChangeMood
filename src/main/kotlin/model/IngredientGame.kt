package org.beijing.model

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