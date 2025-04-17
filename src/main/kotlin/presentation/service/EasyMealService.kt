package org.beijing.presentation.service

import org.beijing.logic.usecases.MealUseCases

fun easyMealSuggestion(
    mealUseCases: MealUseCases
) {
    println("🥗 Easy Meal Suggestions")
    println("------------------------")
    println("✨ These meals are quick (≤30 mins), simple (≤5 ingredients), and easy (≤6 steps)")
    val meals = mealUseCases.easyFoodSuggestionUseCase.easyFoodSuggestion()
    if (meals.isEmpty()) {
        println("😔 Sorry, no meals found for '. Try again later!")
    } else {
        println("\n🍽️ Found ${meals.size} meal(s):\n")
        meals.forEachIndexed { index, meal ->
            println("${index + 1}. ${meal.name} • ⏱️ ${meal.minutes} mins • 🧂 ${meal.nIngredients} ingredients • 🔧 ${meal.nSteps} steps")
        }
    }
}
