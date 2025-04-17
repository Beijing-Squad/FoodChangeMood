package org.beijing.logic

import kotlinx.datetime.LocalDate
import org.beijing.model.Meal

class GetMealOnDateByIdUseCase(
    private val mealRepository: MealRepository
) {

    fun getMealOnDateById(date: LocalDate, id: Int): Meal {
        val meal = mealRepository.getAllMeals()
            .find { it.submitted == date && it.id == id }
        if (meal != null) {
            return meal
        } else {
            throw Exception("No meal found with ID [$id] on the date $date.")
        }
    }

}