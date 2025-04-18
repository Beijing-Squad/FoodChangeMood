package org.beijing.model

enum class GuessStatus(val message: String) {
    TOO_HIGH("Too high! Try a lower number."),
    TOO_LOW("Too low! Try a higher number."),
    CORRECT("Correct!! The preparation time is indeed %d minutes.")
}