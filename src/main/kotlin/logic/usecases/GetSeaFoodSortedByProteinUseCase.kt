package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal


class GetSeaFoodSortedByProteinUseCase(
    private val mealRepository: MealRepository
) {
     private fun getSeaFoodSortedByProteinContent():List<Meal> {
       return mealRepository.getAllMeals()
            .filter {it.tags.contains("seafood")}
            .sortedByDescending{it.nutrition.protein}
    }
    fun showSeaFoodSortedByProtein() {
        println("List Of Sea Food With Protein:")
        println("Rank ---- Meal Name ---- Protein Amount")
        getSeaFoodSortedByProteinContent().forEachIndexed { index, meal ->
            println("${index+1} ---- ${meal.name} ---- ${meal.nutrition.protein.toInt()}")
        }
    }

}