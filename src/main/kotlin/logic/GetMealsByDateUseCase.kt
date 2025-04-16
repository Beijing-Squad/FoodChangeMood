package org.beijing.logic

import kotlinx.datetime.LocalDate

class GetMealsByDateUseCase(
    private val mealRepository: MealRepository,
) {

     fun getMealsByDate(date: LocalDate): List<Pair<Int, String>> {
        val mealsOnDate = mealRepository.getAllMeals()
            .filter { it.submitted == date }
            .map { Pair(it.id, it.name) }

        if (mealsOnDate.isEmpty()) {
            throw Exception("No meals found for the date $date.")
        } else {
            return mealsOnDate
        }
    }

}