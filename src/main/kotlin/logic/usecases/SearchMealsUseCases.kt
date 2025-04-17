package org.beijing.logic.usecases

import kotlinx.datetime.LocalDate
import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class SearchMealsUseCases(
    private val mealRepository: MealRepository,
) {

    // region search by add date and see meal details by id use case feature (8)
    fun getMealsByDate(date: LocalDate): List<Pair<Int, String>> {
        val mealsOnDate = mealRepository.getAllMeals()
            .filter { it.submitted == date }
            .map { Pair(it.id, it.name) }

        if (mealsOnDate.isEmpty()) {
            throw Exception("❌ No Meals Found For The Date [$date].")
        } else {
            return mealsOnDate
        }
    }

    fun getMealOnDateById(date: LocalDate, id: Int): Meal {
        val meal = mealRepository.getAllMeals()
            .find { it.submitted == date && it.id == id }
        if (meal != null) {
            return meal
        } else {
            throw Exception("No meal found with ID [$id] on the date $date.")
        }
    }
    // endregion

}