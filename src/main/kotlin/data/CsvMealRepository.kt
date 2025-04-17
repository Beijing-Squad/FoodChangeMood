package org.beijing.data

import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class CsvMealRepository(
    private val reader: CsvFileReader,
    private val parser: CsvFileParser
) : MealRepository {
    private val cachedMeals: List<Meal> by lazy {
        val csvLines = reader.readLinesFromFile()
        val csvContent = csvLines.joinToString("\n")
        parser.parseCsvFileContent(csvContent)
    }

    override fun getAllMeals(): List<Meal> {
        return cachedMeals
    }
}