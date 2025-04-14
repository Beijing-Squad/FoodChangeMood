package org.beijing.data

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class CsvMealRepository(
    private val reader: CsvFileReader,
    private val parser: CsvFileParser
) : MealRepository {
    override fun getAllMeals(): List<Meal> {
        TODO("Not yet implemented")
    }

}