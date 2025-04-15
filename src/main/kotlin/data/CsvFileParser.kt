package org.beijing.data

import org.beijing.model.Meal
import org.beijing.model.Nutrition
import kotlinx.datetime.LocalDate

class CsvFileParser {
    fun parse(lines: List<String>): List<Meal> {
        return lines.drop(1).mapNotNull { line ->
            val fields = line.split(",", ignoreCase = false, limit = 12)
            try {
                Meal(
                    name = fields[ColumnIndexMeal.NAME],
                    id = fields[ColumnIndexMeal.ID].toInt(),
                    minutes = fields[ColumnIndexMeal.MINUTES].toInt(),
                    contributorId = fields[ColumnIndexMeal.CONTRIBUTOR_ID].toInt(),
                    submitted = parseDate(fields[ColumnIndexMeal.SUBMITTED]),
                    tags = parseList(fields[ColumnIndexMeal.TAGS]),
                    nutrition = parseNutrition(fields[ColumnIndexMeal.NUTRITION]),
                    nSteps = fields[ColumnIndexMeal.N_STEPS].toInt(),
                    steps = parseList(fields[ColumnIndexMeal.STEPS]),
                    description = fields[ColumnIndexMeal.DESCRIPTION].ifBlank { null },
                    ingredients = parseList(fields[ColumnIndexMeal.INGREDIENTS]),
                    nIngredients = fields[ColumnIndexMeal.N_INGREDIENTS].toInt()
                )
            } catch (e: Exception) {
                println("Error parsing line: $line\n${e.message}")
                null
            }
        }
    }

    private fun parseList(str: String): List<String> {
        return str.removeSurrounding("[", "]")
            .split(",")
            .map { it.trim().removeSurrounding("'") }
            .filter { it.isNotBlank() }
    }

    private fun parseNutrition(str: String): Nutrition {
        val values = str.removeSurrounding("[", "]")
            .split(",")
            .map { it.trim().toFloatOrNull() ?: 0f }
        return Nutrition(
            calories = values.getOrElse(ColumnIndexNutrition.CALORIES) { 0f },
            totalFat = values.getOrElse(ColumnIndexNutrition.TOTALFAT) { 0f },
            sugar = values.getOrElse(ColumnIndexNutrition.SUGER) { 0f },
            sodium = values.getOrElse(ColumnIndexNutrition.SODUM) { 0f },
            protein = values.getOrElse(ColumnIndexNutrition.PROTEIN) { 0f },
            saturatedFat = values.getOrElse(ColumnIndexNutrition.SATURATEEDFAT) { 0f },
            carbohydrates = values.getOrElse(ColumnIndexNutrition.CARBOHYDRATES) { 0f },
        )
    }

    private fun parseDate(dateStr: String): LocalDate {
        return try {
            LocalDate.parse(dateStr)
        } catch (e: Exception) {
            LocalDate(1970, 1, 1)
        }
    }

}