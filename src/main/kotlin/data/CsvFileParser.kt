package org.beijing.data

import kotlinx.datetime.LocalDate
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

        val description = if (hasDescription) fields[9] else null
        val ingredients = if (hasDescription) fields[10] else fields[9]
        val ingredientsCount = if (hasDescription) fields[11] else fields[10]
        val submittedDate = LocalDate.parse(fields[4])

        return Meal(
            name = fields[0],
            id = fields[1].toInt(),
            minutes = fields[2].toInt(),
            contributorId = fields[3].toInt(),
            submitted = submittedDate,
            tags = parseListOfStrings(fields[5]),
            nutrition = parseNutritionList(fields[6]),
            nSteps = fields[7].toInt(),
            steps = parseListOfStrings(fields[8]),
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
            calories = values[0],
            totalFat = values[1],
            sugar = values[2],
            sodium = values[3],
            protein = values[4],
            saturatedFat = values[5],
            carbohydrates = values[6]
        )
    }

}