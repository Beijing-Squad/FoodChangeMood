package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal


class GetSortedSeaFoodByProteinUseCase(
    private val mealRepository: MealRepository
) {

    private fun getSortedSeaFoodByProtein(): List<Meal> {
        return mealRepository.getAllMeals()
            .filter { it.tags.contains("seafood") }
            .sortedByDescending { it.nutrition.protein }
    }

    fun showSortedSeaFoodByProtein() {
        println("List Of SeaFood Sorted By Protein:")

        println(String.format("%-6s| %-70s | %-14s", "Rank", "Meal Name", "Protein Content"))
        println("----------------------------------------------------------------------------------------------------")
        getSortedSeaFoodByProtein().forEachIndexed { index, meal ->
            println(String.format("%-6d| %-70s | %-14d", index + 1, meal.name, meal.nutrition.protein.toInt()))
        }
    }

}