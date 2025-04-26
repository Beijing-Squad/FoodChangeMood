package fake

import kotlinx.datetime.LocalDate
import org.beijing.model.Meal
import org.beijing.model.Nutrition

fun createMeal(
    name: String = "Default Meal",
    id: Int = 0,
    minutes: Int = 0,
    contributorId: Int = 0,
    submitted: LocalDate = LocalDate(2023, 1, 1),
    tags: List<String> = listOf("default"),
    nutrition: Nutrition = Nutrition(
        caloriesKcal = 0.0,
        totalFatGrams = 0.0,
        sugarGrams = 0.0,
        sodiumGrams = 0.0,
        proteinGrams = 0.0,
        saturatedFatGrams = 0.0,
        carbohydratesGrams = 0.0
    ),
    nSteps: Int = 0,
    steps: List<String> = listOf("Default Step"),
    description: String? = "Default Description",
    ingredients: List<String> = listOf("Default Ingredient"),
    nIngredients: Int = 0
): Meal {
    return Meal(
        name = name,
        id = id,
        minutes = minutes,
        contributorId = contributorId,
        submitted = submitted,
        tags = tags,
        nutrition = nutrition,
        nSteps = nSteps,
        steps = steps,
        description = description,
        ingredients = ingredients,
        nIngredients = nIngredients
    )
}