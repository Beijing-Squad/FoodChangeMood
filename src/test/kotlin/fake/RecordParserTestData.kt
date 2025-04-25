package fake

import kotlinx.datetime.LocalDate
import org.beijing.model.Meal
import org.beijing.model.Nutrition

object RecordParserTestData {

    fun recordWithAllDataCorrect() = """
            "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']",
            "[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]",
            "4","['Boil water', 'Cook pasta', 'Drain', 'Serve with sauce']",
            "A classic Italian pasta dish.","['Spaghetti', 'Water', 'Salt']","3"
        """

    fun mealWithAllDataCorrect() = Meal(
        name = "Spaghetti",
        id = 123,
        minutes = 30,
        contributorId = 99,
        submitted = LocalDate(year = 2023, monthNumber = 4, dayOfMonth = 21),
        tags = listOf("Italian", "Dinner"),
        nutrition = Nutrition(
            caloriesKcal = 400.0,
            totalFatGrams = 10.0,
            sodiumGrams = 5.0,
            sugarGrams = 800.0,
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
            "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']",
            "[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]",
            "4","['Boil water', 'Cook pasta', 'Drain', 'Serve with sauce']",
            ,"['Spaghetti', 'Water', 'Salt']","3"
        """

    fun mealWithNoDescription() = Meal(
        name = "Spaghetti",
        id = 123,
        minutes = 30,
        contributorId = 99,
        submitted = LocalDate(year = 2023, monthNumber = 4, dayOfMonth = 21),
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
        description = "",
        ingredients = listOf("Spaghetti", "Water", "Salt"),
        nIngredients = 3
    )

    fun recordWithMissingTags() = """
            "Spaghetti","123","30","99","2023-04-21",,
            "[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]",
            "4","['Boil water', 'Cook pasta', 'Drain', 'Serve with sauce']",
            "A classic Italian pasta dish.","['Spaghetti', 'Water', 'Salt']","3"
        """

    fun mealWithMissingTags() = Meal(
        name = "Spaghetti",
        id = 123,
        minutes = 30,
        contributorId = 99,
        submitted = LocalDate(year = 2023, monthNumber = 4, dayOfMonth = 21),
        tags = emptyList(),
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

    fun recordWithMissingNutrition() = """
            "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']",
            ,
            "4","['Boil water', 'Cook pasta', 'Drain', 'Serve with sauce']",
            "A classic Italian pasta dish.","['Spaghetti', 'Water', 'Salt']","3"
        """

    fun mealWithMissingNutrition() = Meal(
        name = "Spaghetti",
        id = 123,
        minutes = 30,
        contributorId = 99,
        submitted = LocalDate(year = 2023, monthNumber = 4, dayOfMonth = 21),
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

    fun recordWithMissingSteps() = """
            "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']",
            "[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]",
            "4",,
            "A classic Italian pasta dish.","['Spaghetti', 'Water', 'Salt']","3"
        """

    fun mealWithMissingSteps() = Meal(
        name = "Spaghetti",
        id = 123,
        minutes = 30,
        contributorId = 99,
        submitted = LocalDate(year = 2023, monthNumber = 4, dayOfMonth = 21),
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
        steps = emptyList(),
        description = "A classic Italian pasta dish.",
        ingredients = listOf("Spaghetti", "Water", "Salt"),
        nIngredients = 3
    )

    fun recordWithMissingStep() = """
            "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']",
            "[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]",
            "4",,
            "A classic Italian pasta dish.","['Spaghetti', '  ', 'Salt']","3"
        """

    fun mealWithMissingStep() = Meal(
        name = "Spaghetti",
        id = 123,
        minutes = 30,
        contributorId = 99,
        submitted = LocalDate(year = 2023, monthNumber = 4, dayOfMonth = 21),
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
        steps = emptyList(),
        description = "A classic Italian pasta dish.",
        ingredients = listOf("Spaghetti", "Salt"),
        nIngredients = 3
    )

    fun recordWithMissingIngredients() = """
            "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']",
            "[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]",
            "4","['Boil water', 'Cook pasta', 'Drain', 'Serve with sauce']",
            "A classic Italian pasta dish.",,"3"
        """

    fun mealWithMissingIngredients() = Meal(
        name = "Spaghetti",
        id = 123,
        minutes = 30,
        contributorId = 99,
        submitted = LocalDate(year = 2023, monthNumber = 4, dayOfMonth = 21),
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
        ingredients = emptyList(),
        nIngredients = 3
    )

    fun recordWithMultiLineDescription() = """
            "Spaghetti","123","30","99","2023-04-21","['Italian', 'Dinner']",
            "[400.0, 10.0, 5.0, 800.0, 15.0, 3.0, 60.0]",
            "4","['Boil water', 'Cook pasta', 'Drain', 'Serve with sauce']",
            "A classic
             Italian pasta
              dish.","['Spaghetti', 'Water', 'Salt']","3"
        """

    fun mealWithMultiLineDescription() = Meal(
        name = "Spaghetti",
        id = 123,
        minutes = 30,
        contributorId = 99,
        submitted = LocalDate(year = 2023, monthNumber = 4, dayOfMonth = 21),
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
        description = """A classic
             Italian pasta
              dish.""",
        ingredients = listOf("Spaghetti", "Water", "Salt"),
        nIngredients = 3
    )

    fun emptyRecord() = ""
}