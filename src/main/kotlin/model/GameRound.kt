package model

import org.beijing.model.Meal

data class GameRound(
    val meal: Meal,
    val attemptsLeft: Int,
    val isCompleted: Boolean,
    val lastFeedBack: String?
    )