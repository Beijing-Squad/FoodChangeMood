package logic.usecases.fakedata


import kotlinx.datetime.LocalDate
import org.beijing.model.Meal
import org.beijing.model.Nutrition


val mealsListWithHighCaloriesMeals = listOf(
    //High Calories
    Meal(
        name = "Creamy Lobster Pasta",
        id = 11,
        minutes = 40,
        contributorId = 201,
        submitted = LocalDate(2024, 5, 10),
        tags = listOf("seafood", "lobster", "pasta", "rich"),
        nutrition = Nutrition(820.0, 38.0, 45.0, 750.0, 60.0, 5.0, 55.0),
        nSteps = 5,
        steps = listOf("Cook pasta", "Prepare sauce", "Add lobster", "Mix and simmer", "Serve with herbs"),
        description = null,
        ingredients = listOf("lobster", "fettuccine", "cream", "garlic", "parsley"),
        nIngredients = 5
    ),
    Meal(
        name = "Fried Fish Platter",
        id = 12,
        minutes = 30,
        contributorId = 202,
        submitted = LocalDate(2024, 6, 3),
        tags = listOf("seafood", "fried", "fish", "comfort"),
        nutrition = Nutrition(900.0, 40.0, 50.0, 1000.0, 70.0, 6.0, 60.0),
        nSteps = 4,
        steps = listOf("Batter fish", "Deep fry", "Drain excess oil", "Serve with sides"),
        description = null,
        ingredients = listOf("white fish", "flour", "oil", "seasoning", "lemon"),
        nIngredients = 5
    ),
    Meal(
        name = "Seafood Alfredo",
        id = 13,
        minutes = 35,
        contributorId = 203,
        submitted = LocalDate(2024, 7, 8),
        tags = listOf("seafood", "pasta", "alfredo"),
        nutrition = Nutrition(780.0, 36.0, 40.0, 850.0, 55.0, 4.0, 50.0),
        nSteps = 5,
        steps = listOf("Cook seafood", "Make Alfredo sauce", "Boil pasta", "Combine all", "Serve hot"),
        description = null,
        ingredients = listOf("shrimp", "scallops", "pasta", "parmesan", "cream"),
        nIngredients = 5
    ),
    Meal(
        name = "Seafood Pizza",
        id = 14,
        minutes = 28,
        contributorId = 204,
        submitted = LocalDate(2024, 8, 15),
        tags = listOf("seafood", "pizza", "cheesy"),
        nutrition = Nutrition(880.0, 34.0, 42.0, 950.0, 65.0, 7.0, 58.0),
        nSteps = 4,
        steps = listOf("Prepare dough", "Add sauce and toppings", "Bake", "Slice and serve"),
        description ="Creamy Alfredo pasta loaded with shrimp and scallops.",
        ingredients = listOf("pizza dough", "shrimp", "squid", "mozzarella", "tomato sauce"),
        nIngredients = 5
    ),
    Meal(
        name = "Stuffed Crab Shells",
        id = 15,
        minutes = 32,
        contributorId = 205,
        submitted = LocalDate(2024, 9, 20),
        tags = listOf("seafood", "crab", "baked"),
        nutrition = Nutrition(760.0, 33.0, 38.0, 700.0, 30.0, 2.0, 45.0),
        nSteps = 5,
        steps = listOf("Prepare crab mixture", "Fill shells", "Bake", "Garnish", "Serve warm"),
        description = "Savory baked crab stuffed in shells with cheesy crust.",
        ingredients = listOf("crab meat", "breadcrumbs", "cheese", "herbs", "butter"),
        nIngredients = 5
    ),
    // Low Calories
    Meal(
        name = "Steamed Tilapia with Veggies",
        id = 16,
        minutes = 25,
        contributorId = 206,
        submitted = LocalDate(2024, 10, 5),
        tags = listOf("seafood", "tilapia", "steamed", "healthy"),
        nutrition = Nutrition(320.0, 28.0, 10.0, 450.0, 8.0, 2.0, 30.0),
        nSteps = 4,
        steps = listOf("Steam tilapia", "Prepare vegetables", "Season lightly", "Serve together"),
        description = "Light and healthy tilapia steamed with fresh vegetables.",
        ingredients = listOf("tilapia", "broccoli", "carrot", "garlic", "lemon"),
        nIngredients = 5
    ),
    Meal(
        name = "Shrimp Lettuce Wraps",
        id = 17,
        minutes = 20,
        contributorId = 207,
        submitted = LocalDate(2024, 11, 12),
        tags = listOf("seafood", "shrimp", "wrap", "low-carb"),
        nutrition = Nutrition(280.0, 25.0, 8.0, 400.0, 5.0, 1.0, 22.0),
        nSteps = 3,
        steps = listOf("Cook shrimp", "Assemble wraps", "Serve with sauce"),
        description = "Low-calorie shrimp wraps in fresh lettuce leaves.",
        ingredients = listOf("shrimp", "lettuce", "soy sauce", "garlic", "lime"),
        nIngredients = 5
    ),
    Meal(
        name = "Grilled Calamari Salad",
        id = 18,
        minutes = 22,
        contributorId = 208,
        submitted = LocalDate(2024, 9, 28),
        tags = listOf("seafood", "squid", "salad", "grilled"),
        nutrition = Nutrition(360.0, 30.0, 12.0, 500.0, 10.0, 2.0, 28.0),
        nSteps = 4,
        steps = listOf("Grill calamari", "Toss with greens", "Add dressing", "Serve chilled"),
        description = "Fresh salad with tender grilled squid and citrus vinaigrette.",
        ingredients = listOf("squid", "arugula", "lemon", "olive oil", "chili flakes"),
        nIngredients = 5
    ),
    Meal(
        name = "Tuna Cucumber Boats",
        id = 19,
        minutes = 15,
        contributorId = 209,
        submitted = LocalDate(2024, 12, 3),
        tags = listOf("seafood", "tuna", "snack", "low-calorie"),
        nutrition = Nutrition(290.0, 22.0, 14.0, 390.0, 4.0, 1.0, 20.0),
        nSteps = 3,
        steps = listOf("Mix tuna filling", "Cut cucumbers", "Spoon filling into cucumber boats"),
        description = "Refreshing cucumber boats filled with seasoned tuna.",
        ingredients = listOf("tuna", "cucumber", "greek yogurt", "dill", "lemon juice"),
        nIngredients = 5
    ),
    Meal(
        name = "Miso Soup with Fish",
        id = 20,
        minutes = 18,
        contributorId = 210,
        submitted = LocalDate(2024, 10, 20),
        tags = listOf("seafood", "soup", "fish", "light"),
        nutrition = Nutrition(250.0, 20.0, 6.0, 300.0, 3.0, 0.0, 18.0),
        nSteps = 4,
        steps = listOf("Boil broth", "Add miso and fish", "Simmer", "Serve hot"),
        description = "Light Japanese-style miso soup with white fish chunks.",
        ingredients = listOf("white fish", "miso paste", "green onion", "tofu", "seaweed"),
        nIngredients = 5
    ),
)


val mealsWithHighCalories = listOf(
    Meal(
        name = "Fried Fish Platter",
        id = 12,
        minutes = 30,
        contributorId = 202,
        submitted = LocalDate(2024, 6, 3),
        tags = listOf("seafood", "fried", "fish", "comfort"),
        nutrition = Nutrition(900.0, 40.0, 50.0, 1000.0, 70.0, 6.0, 60.0),
        nSteps = 4,
        steps = listOf("Batter fish", "Deep fry", "Drain excess oil", "Serve with sides"),
        description = null,
        ingredients = listOf("white fish", "flour", "oil", "seasoning", "lemon"),
        nIngredients = 5
    ),
    Meal(
        name = "Seafood Pizza",
        id = 14,
        minutes = 28,
        contributorId = 204,
        submitted = LocalDate(2024, 8, 15),
        tags = listOf("seafood", "pizza", "cheesy"),
        nutrition = Nutrition(880.0, 34.0, 42.0, 950.0, 65.0, 7.0, 58.0),
        nSteps = 4,
        steps = listOf("Prepare dough", "Add sauce and toppings", "Bake", "Slice and serve"),
        description = null,
        ingredients = listOf("pizza dough", "shrimp", "squid", "mozzarella", "tomato sauce"),
        nIngredients = 5
    ),
    Meal(
        name = "Creamy Lobster Pasta",
        id = 11,
        minutes = 40,
        contributorId = 201,
        submitted = LocalDate(2024, 5, 10),
        tags = listOf("seafood", "lobster", "pasta", "rich"),
        nutrition = Nutrition(820.0, 38.0, 45.0, 750.0, 60.0, 5.0, 55.0),
        nSteps = 5,
        steps = listOf("Cook pasta", "Prepare sauce", "Add lobster", "Mix and simmer", "Serve with herbs"),
        description = null,
        ingredients = listOf("lobster", "fettuccine", "cream", "garlic", "parsley"),
        nIngredients = 5
    ),
    Meal(
        name = "Seafood Alfredo",
        id = 13,
        minutes = 35,
        contributorId = 203,
        submitted = LocalDate(2024, 7, 8),
        tags = listOf("seafood", "pasta", "alfredo"),
        nutrition = Nutrition(780.0, 36.0, 40.0, 850.0, 55.0, 4.0, 50.0),
        nSteps = 5,
        steps = listOf("Cook seafood", "Make Alfredo sauce", "Boil pasta", "Combine all", "Serve hot"),
        description = "Creamy Alfredo pasta loaded with shrimp and scallops.",
        ingredients = listOf("shrimp", "scallops", "pasta", "parmesan", "cream"),
        nIngredients = 5
    ),
    Meal(
        name = "Stuffed Crab Shells",
        id = 15,
        minutes = 32,
        contributorId = 205,
        submitted = LocalDate(2024, 9, 20),
        tags = listOf("seafood", "crab", "baked"),
        nutrition = Nutrition(760.0, 33.0, 38.0, 700.0, 30.0, 2.0, 45.0),
        nSteps = 5,
        steps = listOf("Prepare crab mixture", "Fill shells", "Bake", "Garnish", "Serve warm"),
        description = "Savory baked crab stuffed in shells with cheesy crust.",
        ingredients = listOf("crab meat", "breadcrumbs", "cheese", "herbs", "butter"),
        nIngredients = 5
    ),
)

val mealsWithNoHighCalories = listOf(
    Meal(
        name = "Steamed Tilapia with Veggies",
        id = 16,
        minutes = 25,
        contributorId = 206,
        submitted = LocalDate(2024, 10, 5),
        tags = listOf("seafood", "tilapia", "steamed", "healthy"),
        nutrition = Nutrition(320.0, 28.0, 10.0, 450.0, 8.0, 2.0, 30.0),
        nSteps = 4,
        steps = listOf("Steam tilapia", "Prepare vegetables", "Season lightly", "Serve together"),
        description = "Light and healthy tilapia steamed with fresh vegetables.",
        ingredients = listOf("tilapia", "broccoli", "carrot", "garlic", "lemon"),
        nIngredients = 5
    ),
    Meal(
        name = "Shrimp Lettuce Wraps",
        id = 17,
        minutes = 20,
        contributorId = 207,
        submitted = LocalDate(2024, 11, 12),
        tags = listOf("seafood", "shrimp", "wrap", "low-carb"),
        nutrition = Nutrition(280.0, 25.0, 8.0, 400.0, 5.0, 1.0, 22.0),
        nSteps = 3,
        steps = listOf("Cook shrimp", "Assemble wraps", "Serve with sauce"),
        description = "Low-calorie shrimp wraps in fresh lettuce leaves.",
        ingredients = listOf("shrimp", "lettuce", "soy sauce", "garlic", "lime"),
        nIngredients = 5
    ),
    Meal(
        name = "Grilled Calamari Salad",
        id = 18,
        minutes = 22,
        contributorId = 208,
        submitted = LocalDate(2024, 9, 28),
        tags = listOf("seafood", "squid", "salad", "grilled"),
        nutrition = Nutrition(360.0, 30.0, 12.0, 500.0, 10.0, 2.0, 28.0),
        nSteps = 4,
        steps = listOf("Grill calamari", "Toss with greens", "Add dressing", "Serve chilled"),
        description = "Fresh salad with tender grilled squid and citrus vinaigrette.",
        ingredients = listOf("squid", "arugula", "lemon", "olive oil", "chili flakes"),
        nIngredients = 5
    ),
    Meal(
        name = "Tuna Cucumber Boats",
        id = 19,
        minutes = 15,
        contributorId = 209,
        submitted = LocalDate(2024, 12, 3),
        tags = listOf("seafood", "tuna", "snack", "low-calorie"),
        nutrition = Nutrition(290.0, 22.0, 14.0, 390.0, 4.0, 1.0, 20.0),
        nSteps = 3,
        steps = listOf("Mix tuna filling", "Cut cucumbers", "Spoon filling into cucumber boats"),
        description = "Refreshing cucumber boats filled with seasoned tuna.",
        ingredients = listOf("tuna", "cucumber", "greek yogurt", "dill", "lemon juice"),
        nIngredients = 5
    ),
    Meal(
        name = "Miso Soup with Fish",
        id = 20,
        minutes = 18,
        contributorId = 210,
        submitted = LocalDate(2024, 10, 20),
        tags = listOf("seafood", "soup", "fish", "light"),
        nutrition = Nutrition(250.0, 20.0, 6.0, 300.0, 3.0, 0.0, 18.0),
        nSteps = 4,
        steps = listOf("Boil broth", "Add miso and fish", "Simmer", "Serve hot"),
        description = "Light Japanese-style miso soup with white fish chunks.",
        ingredients = listOf("white fish", "miso paste", "green onion", "tofu", "seaweed"),
        nIngredients = 5
    ),
)
