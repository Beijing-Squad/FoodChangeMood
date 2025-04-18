package org.beijing.logic

import org.beijing.model.Meal

interface MealRepository {
    fun getAllMeals(): List<Meal>
}