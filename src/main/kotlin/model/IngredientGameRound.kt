package org.beijing.model

data class IngredientGameRound(
    val mealName: String,
    val correctAnswer: String,
    val options: List<String>
)