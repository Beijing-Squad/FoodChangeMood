package org.beijing.logic

import kotlinx.datetime.LocalDate
import org.beijing.model.Meal

class GetMealDetailsOnDateByIdUseCase(
    private val mealRepository: MealRepository
) {

    fun getMealDetailsOnDateById(date: LocalDate, id: Int) {
        val meal = mealRepository.getAllMeals()
            .find { it.submitted == date && it.id == id }

        if (meal != null) {
            viewMealDetails(meal)
        } else {
            throw Exception("No meal found with ID $id on the date $date.")
        }
    }

    private fun viewMealDetails(meal: Meal) {
        println("==== Meal Details ====")
        println("Name: ${meal.name}")
        println("Minutes: ${meal.minutes}")
        println("Submitted: ${meal.submitted}")
        println("Tags: ${meal.tags.joinToString(", ")}")
        println("Nutrition: ${meal.nutrition}")
        println("Steps: ${meal.steps.joinToString(", ")}")
        if (meal.description == null) {
            println("Description: No description available.")
        } else {
            println("Description: ${meal.description}")
        }
        println("Ingredients: ${meal.ingredients.joinToString(", ")}")
    }
}