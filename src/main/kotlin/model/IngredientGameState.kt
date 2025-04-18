package org.beijing.model

data class IngredientGameState(
    val score: Int = 0,
    val correctAnswers: Int = 0,
    val usedMeals: Set<Int> = emptySet()
)