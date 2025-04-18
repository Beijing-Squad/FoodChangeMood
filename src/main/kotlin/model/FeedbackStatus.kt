package org.beijing.model

enum class FeedbackStatus(val message: String) {
    NO_ATTEMPTS_LEFT("No Attempts Left, The Actual Preparation Time is: %d minutes."),
    ROUND_ALREADY_COMPLETED("This round is already Completed, Start A new Round."),
    GAME_OVER("GameOver! The actual preparation time is %d minutes."),
}