package org.beijing.data

import kotlinx.datetime.LocalDate
import org.beijing.data.utils.ColumnIndex
import org.beijing.data.utils.NutritionIndex
import org.beijing.model.Meal
import org.beijing.model.Nutrition

class CsvFileParser {

    fun parseCsvFileContent(fileContent: String): List<Meal> {
        val csvLines = splitCsvIntoLines(fileContent)

        return csvLines.drop(1).map { line -> parseCsvLineToMeal(line) }
    }

    private fun parseCsvLineToMeal(line: String): Meal {
        val fields = splitLineByCommas(line)

        val hasDescription = fields.size == 12

        val description = if (hasDescription) fields[ColumnIndex.DESCRIPTION] else null
        val ingredients = if (hasDescription) fields[ColumnIndex.INGREDIENTS] else fields[9]
        val ingredientsCount = if (hasDescription) fields[ColumnIndex.N_INGREDIENTS] else fields[10]
        val submittedDate = LocalDate.parse(fields[ColumnIndex.SUBMITTED])

        return Meal(
            name = fields[ColumnIndex.NAME],
            id = fields[ColumnIndex.ID].toInt(),
            minutes = fields[ColumnIndex.MINUTES].toInt(),
            contributorId = fields[ColumnIndex.CONTRIBUTOR_ID].toInt(),
            submitted = submittedDate,
            tags = parseListOfStrings(fields[ColumnIndex.TAGS]),
            nutrition = parseNutritionList(fields[ColumnIndex.NUTRITION]),
            nSteps = fields[ColumnIndex.N_STEPS].toInt(),
            steps = parseListOfStrings(fields[ColumnIndex.STEPS]),
            description = description,
            ingredients = parseListOfStrings(ingredients),
            nIngredients = ingredientsCount.toInt()
        )
    }

    private fun splitCsvIntoLines(csvContent: String): List<String> {
        val lines = mutableListOf<String>()
        var currentLine = ""
        var insideQuotes = false

        for (char in csvContent) {
            when {
                char == '"' -> {
                    insideQuotes = !insideQuotes
                    currentLine += char
                }

                char == '\n' && !insideQuotes -> {
                    lines.add(currentLine)
                    currentLine = ""
                }

                else -> {
                    currentLine += char
                }
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }

        return lines
    }

    private fun splitLineByCommas(csvLine: String): List<String> {
        return Regex(""",(?=(?:[^"]*"[^"]*")*[^"]*$)""")
            .split(csvLine)
            .map { it.trim().removeSurrounding("\"") }
    }

    private fun parseListOfStrings(rawList: String): List<String> {
        return rawList
            .removePrefix("['")
            .removeSuffix("']")
            .split("', '")
            .map { it.trim().removeSurrounding("'") }
            .filter { it.isNotBlank() }
    }

    private fun parseNutritionList(rawNutrition: String): Nutrition {
        val values = rawNutrition
            .removePrefix("[")
            .removeSuffix("]")
            .split(",")
            .map { it.trim().toDouble() }

        return Nutrition(
            caloriesKcal = values[NutritionIndex.CALORIES],
            totalFatGrams = values[NutritionIndex.TOTAL_FAT],
            sugarGrams = values[NutritionIndex.SUGAR],
            sodiumGrams = values[NutritionIndex.SODIUM],
            proteinGrams = values[NutritionIndex.PROTEIN],
            saturatedFatGrams = values[NutritionIndex.SATURATED_FAT],
            carbohydratesGrams = values[NutritionIndex.CARBOHYDRATES]
        )
    }

}