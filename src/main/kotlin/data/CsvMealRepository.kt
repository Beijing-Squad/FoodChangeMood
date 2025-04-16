package org.beijing.data

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class CsvMealRepository(
    private val reader: CsvFileReader,
    private val parser: CsvFileParser
) : MealRepository {

    override fun getAllMeals(): List<Meal> {
        val allMeals: MutableList<Meal> = mutableListOf()

        val csvLines = reader.readLinesFromFile()
        val csvContent = csvLines.joinToString("\n")
        val mealList = parser.parseCsvFileContent(csvContent)

        allMeals.addAll(mealList)
        return allMeals
    }
}