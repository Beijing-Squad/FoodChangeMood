package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal


class GetSeaFoodWithProteinUseCase(
    private val mealRepository: MealRepository
) {
     fun getSeaFoodWithProtein():List<Meal> {
       return mealRepository.getAllMeals()
            .filter {it.tags.contains("seafood")}
            .sortedByDescending{it.nutrition.protein}
    }
    fun showSeaFoodWithProtein() {
        println("List Of Sea Food With Protein:")
        println("Rank ---- Meal Name ---- Protein Amount")
        getSeaFoodWithProtein().forEachIndexed { index, meal ->
            println("${index+1} ---- ${meal.name} ---- ${meal.nutrition.protein.toInt()}")
        }
    }

}