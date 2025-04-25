package fake

import kotlinx.datetime.LocalDate
import org.beijing.model.Meal
import org.beijing.model.Nutrition

object RecordParserTestData {

    fun recordWithAllDataCorrect() = """
        name,id,minutes,contributorId,submitted,tags,nutrition,nSteps,steps,description,ingredients,nIngredients
        "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']","[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]","4","['Boil water', 'Cook pasta', 'Drain', 'Serve with sauce']","A classic Italian pasta dish.","['Spaghetti', 'Water', 'Salt']","3"
    """.trimIndent()

    fun mealWithAllDataCorrect() = Meal(
        name = "Spaghetti",
        id = 123,
        minutes = 30,
        contributorId = 99,
        submitted = LocalDate(2023, 4, 21),
        tags = listOf("Italian", "Dinner"),
        nutrition = Nutrition(
            caloriesKcal = 400.0,
            totalFatGrams = 10.0,
            sugarGrams = 5.0,
            sodiumGrams = 800.0,
            proteinGrams = 15.0,
            saturatedFatGrams = 3.0,
            carbohydratesGrams = 60.0
        ),
        nSteps = 4,
        steps = listOf("Boil water", "Cook pasta", "Drain", "Serve with sauce"),
        description = "A classic Italian pasta dish.",
        ingredients = listOf("Spaghetti", "Water", "Salt"),
        nIngredients = 3
    )

    fun recordWithNoDescription() = """
        name,id,minutes,contributorId,submitted,tags,nutrition,nSteps,steps,description,ingredients,nIngredients
        "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']","[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]","4","['Boil water', 'Cook pasta', 'Drain', 'Serve with sauce']","","['Spaghetti', 'Water', 'Salt']","3"
    """.trimIndent()

    fun mealWithNoDescription() = mealWithAllDataCorrect().copy(description = "")

    fun recordWithMissingTags() = """
        name,id,minutes,contributorId,submitted,tags,nutrition,nSteps,steps,description,ingredients,nIngredients
        "Spaghetti","123","30","99","2023-04-21","[]","[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]","4","['Boil water', 'Cook pasta', 'Drain', 'Serve with sauce']","A classic Italian pasta dish.","['Spaghetti', 'Water', 'Salt']","3"
    """.trimIndent()

    fun mealWithMissingTags() = mealWithAllDataCorrect().copy(tags = emptyList())

    fun recordWithMissingNutrition() = """
        name,id,minutes,contributorId,submitted,tags,nutrition,nSteps,steps,description,ingredients,nIngredients
        "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']","[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]","4","['Boil water', 'Cook pasta', 'Drain', 'Serve with sauce']","A classic Italian pasta dish.","['Spaghetti', 'Water', 'Salt']","3"
    """.trimIndent()

    fun mealWithMissingNutrition() = mealWithAllDataCorrect().copy(
        nutrition = Nutrition(
            caloriesKcal = 0.0,
            totalFatGrams = 0.0,
            sugarGrams = 0.0,
            sodiumGrams = 0.0,
            proteinGrams = 0.0,
            saturatedFatGrams = 0.0,
            carbohydratesGrams = 0.0
        )
    )

    fun recordWithMissingSteps() = """
        name,id,minutes,contributorId,submitted,tags,nutrition,nSteps,steps,description,ingredients,nIngredients
        "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']","[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]","4","[]","A classic Italian pasta dish.","['Spaghetti', 'Water', 'Salt']","3"
    """.trimIndent()

    fun mealWithMissingSteps() = mealWithAllDataCorrect().copy(steps = emptyList())

    fun recordWithMissingStep() = """
        name,id,minutes,contributorId,submitted,tags,nutrition,nSteps,steps,description,ingredients,nIngredients
        "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']","[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]","4","['Boil water', 'Cook pasta', 'Drain', 'Serve with sauce']","A classic Italian pasta dish.","['Spaghetti', 'Salt']","2"
    """.trimIndent()

    fun mealWithMissingStep() = mealWithAllDataCorrect().copy(
        ingredients = listOf("Spaghetti", "Salt"),
        nIngredients = 2
    )

    fun recordWithMissingIngredients() = """
        name,id,minutes,contributorId,submitted,tags,nutrition,nSteps,steps,description,ingredients,nIngredients
        "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']","[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]","4","['Boil water', 'Cook pasta', 'Drain', 'Serve with sauce']","A classic Italian pasta dish.","[]","0"
    """.trimIndent()

    fun mealWithMissingIngredients() = mealWithAllDataCorrect().copy(
        ingredients = emptyList(),
        nIngredients = 0
    )

    fun recordWithMultiLineDescription() = """
        name,id,minutes,contributorId,submitted,tags,nutrition,nSteps,steps,description,ingredients,nIngredients
        "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']","[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]","4","['Boil water', 'Cook pasta', 'Drain', 'Serve with sauce']","A classic\nItalian pasta\ndish.","['Spaghetti', 'Water', 'Salt']","3"
    """.trimIndent()

    fun mealWithMultiLineDescription() = mealWithAllDataCorrect().copy(
        description = "A classic\nItalian pasta\ndish."
    )

}