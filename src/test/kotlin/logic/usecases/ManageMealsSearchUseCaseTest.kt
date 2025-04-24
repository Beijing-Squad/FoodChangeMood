package logic.usecases

import io.mockk.every
import io.mockk.mockk
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsSearchUseCase
import org.beijing.model.Meal
import org.beijing.model.Nutrition
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlinx.datetime.LocalDate

class ManageMealsSearchUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: ManageMealsSearchUseCase

    private fun createMeal(
        id: Int,
        name: String,
        tags: List<String>,
        minutes: Int,
        contributorId: Int,
        submitted: LocalDate = LocalDate(2023, 5, 1),
        caloriesKcal: Double = 100.0,
        totalFatGrams: Double = 5.0,
        sugarGrams: Double = 5.0,
        sodiumGrams: Double = 200.0,
        proteinGrams: Double = 10.0,
        saturatedFatGrams: Double = 2.0,
        carbohydratesGrams: Double = 20.0,
        nSteps: Int = 5,
        steps: List<String> = listOf("Step 1", "Step 2", "Step 3"),
        description: String? = "Meal description",
        ingredients: List<String> = listOf("Ingredient 1", "Ingredient 2"),
        nIngredients: Int = 2
    ): Meal {
        val nutrition = Nutrition(
            caloriesKcal = caloriesKcal,
            totalFatGrams = totalFatGrams,
            sugarGrams = sugarGrams,
            sodiumGrams = sodiumGrams,
            proteinGrams = proteinGrams,
            saturatedFatGrams = saturatedFatGrams,
            carbohydratesGrams = carbohydratesGrams
        )
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

    @BeforeEach
    fun setup() {
        mealRepository = mockk()
        useCase = ManageMealsSearchUseCase(mealRepository)
    }

    //region get meal by date
    @Test
    fun getMealsByDate() {
    }
//endregion

    //region get meal by date and id
    @Test
    fun getMealByDateAndId() {
    }
//endregion

    //region get gym helper meals by calories
    @Test
    fun getGymHelperMealsByCaloriesAndProtein() {
    }
//endregion

    // region get meal by name
    @Test
    fun getMealByName() {
    }
//endregion

    //region get meal by country
    @Test
    fun `should return meals matching country query sorted by name`() {
        // Given
        val meals = listOf(
            createMeal(id = 1, name = "Zaatar Pizza", tags = listOf("Lebanese"), minutes = 10, contributorId = 1),
            createMeal(id = 2, name = "Baklava", tags = listOf("Lebanese"), minutes = 5, contributorId = 2),
            createMeal(id = 3, name = "Kebab", tags = listOf("Lebanese"), minutes = 20, contributorId = 3)
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.getMealByCountry("lebanese")

        // Then
        assert(result.map { it.name } == listOf("Baklava", "Kebab", "Zaatar Pizza"))
    }

    @Test
    fun `should return empty list when no meals match the country query`() {
        // Given
        val meals = listOf(
            createMeal(id = 1, name = "Pasta", tags = listOf("italian"),minutes = 10, contributorId = 1),
            createMeal(id = 2, name = "Burger", tags = listOf("american"), minutes = 20, contributorId = 2)
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.getMealByCountry("japanese")

        // Then
        assert(result.isEmpty())
    }

    @Test
    fun `should return at most 20 meals when more than 20 match`() {
        // Given
        val meals = List(50) { index -> createMeal(id = index, name = "Iraqi Meal $index", tags = listOf("iraqi"), minutes = 10, contributorId = 1) }
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.getMealByCountry("iraqi")

        // Then
        assert(result.size == 20)
    }

    @Test
    fun `should match country query case-insensitively`() {
        // Given
        val meals = listOf(
            createMeal(id = 1, name = "Rice Dish", tags = listOf("Iraqi"), minutes = 10, contributorId = 1),
            createMeal(id = 2, name = "Soup", tags = listOf("iraqi"), minutes = 20, contributorId = 2)
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.getMealByCountry("IRAQI")

        // Then
        assert(result.size == 2)
    }
    //endregion

    //region get iraqi meals
    @Test
    fun getIraqiMeals() {
    }
    //endregion
}