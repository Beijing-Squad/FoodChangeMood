package org.beijing.presentation.utils

enum class MealServiceError(val message: String) {
    NULL_MEAL_NAME_INPUT("❌ Meal name input cannot be null."),
    EMPTY_MEAL_NAME_INPUT("❌ Meal name input cannot be empty."),
    INVALID_MEAL_NAME("❌ Meal name must contain only letters and spaces."),
    INVALID_DATE_FORMAT("❌ Invalid Date Format, Please Use (YYYY-MM-DD)."),
    INVALID_ID_FORMAT("Invalid ID Format, Please Use A Number."),
    INVALID_TARGETS("\nPlease ensure that both Calories and Protein inputs are positive values.")
}