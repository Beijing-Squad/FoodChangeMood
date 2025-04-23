package org.beijing.presentation

import org.beijing.model.Meal
import org.beijing.model.Nutrition

class ConsoleViewMealDetails() : ViewMealDetails {
    override fun displayMealDetails(meal: Meal) {
        println("╔════════════════════════════════════╗")
        println("║          🍽️ Meal Details           ║")
        println("╚════════════════════════════════════╝")
        println("🍽 Name : ${meal.name}")
        println("⏱️ Preparation Time : ${meal.minutes} minutes")
        println("\n📊 Nutrition Facts:")
        displayNutrition(meal.nutrition)
        println()
        displaySteps(meal.steps)
        println("\n📄 Description:")
        if (meal.description.isNullOrBlank()) {
            println("  • No Description Available.")
        } else {
            println("  • ${meal.description}")
        }
        println()
        displayIngredients(meal.ingredients)
        println("══════════════════════════════════════")
    }

    private fun displayNutrition(nutrition: Nutrition) {
        println("   • Calories       : ${nutrition.caloriesKcal} kcal")
        println("   • Total Fat      : ${nutrition.totalFatGrams} g")
        println("   • Sugar          : ${nutrition.sugarGrams} g")
        println("   • Sodium         : ${nutrition.sodiumGrams} mg")
        println("   • Protein        : ${nutrition.proteinGrams} g")
        println("   • Saturated Fat  : ${nutrition.saturatedFatGrams} g")
        println("   • Carbohydrates  : ${nutrition.carbohydratesGrams} g")
    }

    private fun displaySteps(steps: List<String>) {
        println("📝 Steps (${steps.size}):")
        steps.forEachIndexed { index, step ->
            println("   ${index + 1}. $step")
        }
    }

    private fun displayIngredients(ingredients: List<String>) {
        println("🧂 Ingredients (${ingredients.size}):")
        ingredients.forEach { ingredient ->
            println("   - $ingredient")
        }
    }
}
