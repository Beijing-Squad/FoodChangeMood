package helper

import org.beijing.model.Meal
import org.beijing.model.Nutrition

fun mealWithTags(id: Int, tags: List<String>): Meal {
    return Meal(
        id = id,
        name = "Meal $id",
        minutes = 30,
        contributorId = 1,
        submitted = kotlinx.datetime.LocalDate(2024, 1, 1),
        tags = tags,
        nutrition = Nutrition(500.0, 10.0, 5.0, 1.0, 30.0, 5.0, 20.0),
        nSteps = 3,
        steps = listOf("Step 1", "Step 2"),
        description = "Sample",
        ingredients = listOf("Ingredient 1"),
        nIngredients = 1
    )
}