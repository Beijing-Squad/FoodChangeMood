package org.beijing.logic.usecases

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class GetTenRandomPotatoMealsUseCase(
    private val mealRepository: MealRepository
) {


    fun getTenRandomMealsContainsPotato(): List<Meal> {
        return mealRepository.getAllMeals().asSequence().filter { meal ->
            meal.ingredients.any { ingredient ->
                ingredient.contains("Potato", true)
            }
        }.shuffled().take(10).toList()
    }

    fun showTenRandomPotatoMeals() {
        val tenRandomPotatoMeals = getTenRandomMealsContainsPotato()

        if (tenRandomPotatoMeals.isEmpty()) {
            println("There is no meals contains potato in their ingredients")
        } else {
            println("\uD83C\uDF55\uD83C\uDF54\uD83C\uDF57List of ten random Meals with potato in their ingredients\uD83C\uDF55\uD83C\uDF54\uD83C\uDF57")
            println("-".repeat(40))
            println(
                "Rank".padEnd(5) + "| " + "Meal Name".padEnd(70)
            )

            tenRandomPotatoMeals.forEachIndexed { index, meal ->
                println(
                    "${index + 1}".padEnd(5) + "| " + meal.name
                )
            }
        }
    }
}