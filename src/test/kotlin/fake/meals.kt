package fake

import kotlinx.datetime.LocalDate
import org.beijing.model.Meal
import org.beijing.model.Nutrition


val meals = listOf(
    Meal(
        name = "Grilled Chicken Salad",
        id = 1,
        minutes = 25,
        contributorId = 101,
        submitted = LocalDate(2023, 5, 10),
        tags = listOf("healthy", "salad", "chicken"),
        nutrition = Nutrition(300.0, 15.0, 5.0, 600.0, 100.0, 3.0, 20.0),
        nSteps = 4,
        steps = listOf("Grill chicken", "Chop vegetables", "Mix ingredients", "Add dressing"),
        description = "A fresh and protein-packed salad.",
        ingredients = listOf("chicken breast", "lettuce", "tomato", "cucumber", "olive oil"),
        nIngredients = 5
    ),
    Meal(
        name = "Spaghetti Bolognese",
        id = 2,
        minutes = 45,
        contributorId = 102,
        submitted = LocalDate(2023, 6, 15),
        tags = listOf("pasta", "italian", "dinner"),
        nutrition = Nutrition(600.0, 20.0, 10.0, 800.0, 25.0, 8.0, 70.0),
        nSteps = 6,
        steps = listOf("Cook pasta", "Brown beef", "Add sauce", "Simmer", "Combine", "Serve"),
        description = "Classic Italian pasta dish.",
        ingredients = listOf("spaghetti", "ground beef", "tomato sauce", "onion", "garlic"),
        nIngredients = 5
    ),
    Meal(
        name = "Avocado Toast",
        id = 3,
        minutes = 10,
        contributorId = 103,
        submitted = LocalDate(2023, 7, 20),
        tags = listOf("breakfast", "vegan", "quick"),
        nutrition = Nutrition(250.0, 15.0, 2.0, 300.0, 5.0, 2.0, 20.0),
        nSteps = 3,
        steps = listOf("Toast bread", "Mash avocado", "Spread and season"),
        description = "Simple and nutritious breakfast.",
        ingredients = listOf("bread", "avocado", "lemon juice", "salt"),
        nIngredients = 4
    ),
    Meal(
        name = "Beef Stir-Fry",
        id = 4,
        minutes = 30,
        contributorId = 104,
        submitted = LocalDate(2023, 8, 5),
        tags = listOf("asian", "beef", "dinner"),
        nutrition = Nutrition(500.0, 25.0, 8.0, 700.0, 30.0, 6.0, 40.0),
        nSteps = 5,
        steps = listOf("Slice beef", "Chop vegetables", "Stir-fry beef", "Add veggies", "Season"),
        description = "Quick and flavorful beef dish.",
        ingredients = listOf("beef", "bell pepper", "soy sauce", "garlic", "ginger"),
        nIngredients = 5
    ),
    Meal(
        name = "Blueberry Muffins",
        id = 5,
        minutes = 35,
        contributorId = 105,
        submitted = LocalDate(2023, 9, 12),
        tags = listOf("baking", "breakfast", "sweet"),
        nutrition = Nutrition(200.0, 8.0, 15.0, 200.0, 4.0, 4.0, 30.0),
        nSteps = 4,
        steps = listOf("Mix dry ingredients", "Mix wet ingredients", "Combine and add blueberries", "Bake"),
        description = "Soft and sweet muffins.",
        ingredients = listOf("flour", "blueberries", "sugar", "butter", "egg"),
        nIngredients = 5
    ),
    Meal(
        name = "Vegetable Curry",
        id = 6,
        minutes = 40,
        contributorId = 106,
        submitted = LocalDate(2023, 10, 1),
        tags = listOf("vegan", "indian", "spicy"),
        nutrition = Nutrition(400.0, 15.0, 10.0, 500.0, 10.0, 5.0, 60.0),
        nSteps = 5,
        steps = listOf("Chop vegetables", "Sauté spices", "Add veggies", "Simmer with coconut milk", "Serve"),
        description = "Aromatic and hearty curry.",
        ingredients = listOf("potato", "carrot", "coconut milk", "curry powder", "onion"),
        nIngredients = 5
    ),
    Meal(
        name = "Chocolate Chip Cookies",
        id = 7,
        minutes = 25,
        contributorId = 107,
        submitted = LocalDate(2023, 11, 8),
        tags = listOf("dessert", "baking", "sweet"),
        nutrition = Nutrition(150.0, 8.0, 12.0, 100.0, 2.0, 4.0, 20.0),
        nSteps = 4,
        steps = listOf("Mix butter and sugar", "Add eggs and flour", "Stir in chips", "Bake"),
        description = "Classic chewy cookies.",
        ingredients = listOf("flour", "chocolate chips", "butter", "sugar", "egg"),
        nIngredients = 5
    ),
    Meal(
        name = "Greek Yogurt Parfait",
        id = 8,
        minutes = 10,
        contributorId = 108,
        submitted = LocalDate(2023, 12, 15),
        tags = listOf("breakfast", "healthy", "quick"),
        nutrition = Nutrition(200.0, 5.0, 15.0, 50.0, 10.0, 2.0, 30.0),
        nSteps = 3,
        steps = listOf("Layer yogurt", "Add granola", "Top with fruit"),
        description = "Creamy and crunchy parfait.",
        ingredients = listOf("greek yogurt", "granola", "berries", "honey"),
        nIngredients = 4
    ),
    Meal(
        name = "Margherita Pizza",
        id = 9,
        minutes = 60,
        contributorId = 109,
        submitted = LocalDate(2024, 1, 20),
        tags = listOf("italian", "pizza", "dinner"),
        nutrition = Nutrition(700.0, 25.0, 5.0, 900.0, 20.0, 10.0, 80.0),
        nSteps = 6,
        steps = listOf("Make dough", "Knead", "Rest dough", "Add toppings", "Bake", "Slice"),
        description = "Classic Italian pizza.",
        ingredients = listOf("flour", "tomato", "mozzarella", "basil", "yeast"),
        nIngredients = 5
    ),
    Meal(
        name = "Lentil Soup",
        id = 10,
        minutes = 50,
        contributorId = 110,
        submitted = LocalDate(2024, 2, 10),
        tags = listOf("vegan", "soup", "healthy"),
        nutrition = Nutrition(300.0, 5.0, 5.0, 400.0, 15.0, 1.0, 50.0),
        nSteps = 5,
        steps = listOf("Sauté onion", "Add lentils", "Pour broth", "Simmer", "Season"),
        description = "Hearty and warming soup.",
        ingredients = listOf("lentils", "onion", "carrot", "celery", "broth"),
        nIngredients = 5
    ),
    Meal(
        name = "Tacos",
        id = 11,
        minutes = 20,
        contributorId = 111,
        submitted = LocalDate(2024, 3, 5),
        tags = listOf("mexican", "quick", "dinner"),
        nutrition = Nutrition(400.0, 20.0, 3.0, 600.0, 15.0, 7.0, 30.0),
        nSteps = 4,
        steps = listOf("Cook beef", "Warm tortillas", "Chop toppings", "Assemble"),
        description = "Flavorful street-style tacos.",
        ingredients = listOf("ground beef", "tortillas", "lettuce", "cheese", "salsa"),
        nIngredients = 5
    ),
    Meal(
        name = "Banana Smoothie",
        id = 12,
        minutes = 5,
        contributorId = 112,
        submitted = LocalDate(2024, 4, 1),
        tags = listOf("breakfast", "smoothie", "quick"),
        nutrition = Nutrition(200.0, 3.0, 20.0, 50.0, 5.0, 1.0, 35.0),
        nSteps = 2,
        steps = listOf("Add ingredients to blender", "Blend until smooth"),
        description = "Creamy and refreshing drink.",
        ingredients = listOf("banana", "milk", "yogurt", "honey"),
        nIngredients = 4
    ),
    Meal(
        name = "Roasted Salmon",
        id = 13,
        minutes = 30,
        contributorId = 113,
        submitted = LocalDate(2024, 5, 15),
        tags = listOf("seaMeal", "healthy", "dinner"),
        nutrition = Nutrition(450.0, 25.0, 0.0, 500.0, 35.0, 5.0, 0.0),
        nSteps = 4,
        steps = listOf("Season salmon", "Preheat oven", "Roast", "Serve"),
        description = "Tender and flavorful fish.",
        ingredients = listOf("salmon", "olive oil", "lemon", "garlic"),
        nIngredients = 4
    ),
    Meal(
        name = "Pancakes",
        id = 14,
        minutes = 20,
        contributorId = 114,
        submitted = LocalDate(2024, 6, 10),
        tags = listOf("breakfast", "sweet", "quick"),
        nutrition = Nutrition(300.0, 10.0, 10.0, 400.0, 8.0, 4.0, 45.0),
        nSteps = 4,
        steps = listOf("Mix batter", "Heat pan", "Pour batter", "Flip and cook"),
        description = "Fluffy breakfast pancakes.",
        ingredients = listOf("flour", "milk", "egg", "sugar", "butter"),
        nIngredients = 5
    ),
    Meal(
        name = "Caesar Salad",
        id = 15,
        minutes = 15,
        contributorId = 115,
        submitted = LocalDate(2024, 7, 20),
        tags = listOf("salad", "healthy", "quick"),
        nutrition = Nutrition(250.0, 15.0, 2.0, 600.0, 5.0, 3.0, 15.0),
        nSteps = 3,
        steps = listOf("Chop lettuce", "Make dressing", "Toss with croutons"),
        description = "Classic creamy salad.",
        ingredients = listOf("romaine lettuce", "croutons", "parmesan", "caesar dressing"),
        nIngredients = 4
    ),
    Meal(
        name = "Chili Con Carne",
        id = 16,
        minutes = 60,
        contributorId = 116,
        submitted = LocalDate(2024, 8, 5),
        tags = listOf("spicy", "dinner", "beef"),
        nutrition = Nutrition(500.0, 20.0, 8.0, 700.0, 25.0, 7.0, 40.0),
        nSteps = 5,
        steps = listOf("Brown beef", "Add spices", "Add beans", "Simmer", "Serve"),
        description = "Hearty and spicy chili.",
        ingredients = listOf("ground beef", "kidney beans", "tomato", "chili powder", "onion"),
        nIngredients = 5
    ),
    Meal(
        name = "Fruit Salad",
        id = 17,
        minutes = 10,
        contributorId = 117,
        submitted = LocalDate(2024, 9, 10),
        tags = listOf("vegan", "healthy", "quick"),
        nutrition = Nutrition(150.0, 1.0, 25.0, 20.0, 2.0, 0.0, 35.0),
        nSteps = 3,
        steps = listOf("Chop fruit", "Mix in bowl", "Add lemon juice"),
        description = "Refreshing and colorful salad.",
        ingredients = listOf("apple", "banana", "strawberry", "lemon juice"),
        nIngredients = 4
    ),
    Meal(
        name = "Fried Rice",
        id = 18,
        minutes = 25,
        contributorId = 118,
        submitted = LocalDate(2024, 10, 15),
        tags = listOf("asian", "quick", "dinner"),
        nutrition = Nutrition(350.0, 10.0, 3.0, 500.0, 10.0, 2.0, 50.0),
        nSteps = 4,
        steps = listOf("Cook rice", "Sauté vegetables", "Add rice", "Season"),
        description = "Simple and versatile rice dish.",
        ingredients = listOf("rice", "carrot", "peas", "soy sauce", "egg"),
        nIngredients = 5
    ),
    Meal(
        name = "Apple Pie",
        id = 19,
        minutes = 90,
        contributorId = 119,
        submitted = LocalDate(2024, 11, 1),
        tags = listOf("dessert", "baking", "sweet"),
        nutrition = Nutrition(400.0, 20.0, 30.0, 300.0, 4.0, 10.0, 50.0),
        nSteps = 6,
        steps = listOf("Make dough", "Peel apples", "Mix filling", "Assemble pie", "Bake", "Cool"),
        description = "Classic American dessert.",
        ingredients = listOf("flour", "apple", "sugar", "butter", "cinnamon"),
        nIngredients = 5
    ),
    Meal(
        name = "Caprese Salad",
        id = 20,
        minutes = 10,
        contributorId = 120,
        submitted = LocalDate(2024, 12, 5),
        tags = listOf("italian", "healthy", "quick"),
        nutrition = Nutrition(200.0, 15.0, 3.0, 400.0, 10.0, 5.0, 10.0),
        nSteps = 3,
        steps = listOf("Slice tomato and mozzarella", "Layer with basil", "Drizzle oil"),
        description = "Fresh and simple Italian salad.",
        ingredients = listOf("tomato", "mozzarella", "basil", "olive oil"),
        nIngredients = 4
    )
)

fun createMeal(
    name: String,
    id: Int,
    caloriesKcal: Double,
    protein: Double
): Meal {
    return Meal(
        name = name,
        id = id,
        minutes = 0,
        contributorId = 0,
        submitted = LocalDate(2023, 1, 1),
        tags = emptyList(),
        nutrition = Nutrition(
            caloriesKcal = caloriesKcal,
            proteinGrams = protein,
            totalFatGrams = 0.0,
            sodiumGrams = 0.0,
            saturatedFatGrams = 0.0,
            carbohydratesGrams = 0.0,
            sugarGrams = 0.0
        ),
        nSteps = 0,
        steps = emptyList(),
        description = "",
        ingredients = emptyList(),
        nIngredients = 0
    )
}

fun createMealForSearchByName(
    name: String,
    id: Int,
): Meal {
    return Meal(
        name = name,
        id = id,
        minutes = 0,
        contributorId = 0,
        submitted = LocalDate(2023, 1, 1),
        tags = emptyList(),
        nutrition = Nutrition(
            caloriesKcal = 0.0,
            proteinGrams = 0.0,
            totalFatGrams = 0.0,
            sodiumGrams = 0.0,
            saturatedFatGrams = 0.0,
            carbohydratesGrams = 0.0,
            sugarGrams = 0.0
        ),
        nSteps = 0,
        steps = emptyList(),
        description = "",
        ingredients = emptyList(),
        nIngredients = 0
    )
}