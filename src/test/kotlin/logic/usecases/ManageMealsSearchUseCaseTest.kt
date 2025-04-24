package logic.usecases

import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsSearchUseCase
import org.beijing.model.Meal
import org.beijing.model.Nutrition
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows

class ManageMealsSearchUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: ManageMealsSearchUseCase

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
    private val fakeMeals = listOf(
        Meal(
            name = "Spicy Chili Chicken Bowl",
            id = 1,
            minutes = 15,
            contributorId = 1,
            submitted = LocalDate.parse("2025-01-01"),
            tags = listOf("spicy", "bowl"),
            nutrition = Nutrition(
                caloriesKcal = 300.0,
                totalFatGrams = 10.0,
                sugarGrams = 3.0,
                sodiumGrams = 250.0,
                proteinGrams = 30.0,
                saturatedFatGrams = 2.0,
                carbohydratesGrams = 20.0
            ),
            nSteps = 3,
            steps = listOf("Grill chicken", "Add chili sauce", "Serve"),
            description = "A spicy and flavorful meal",
            ingredients = listOf("chicken", "chili", "rice"),
            nIngredients = 3
        ),
        Meal(
            name = "Beef Burger",
            id = 2,
            minutes = 60,
            contributorId = 2,
            submitted = LocalDate.parse("2025-02-01"),
            tags = listOf("hearty", "dinner"),
            nutrition = Nutrition(
                caloriesKcal = 550.0,
                totalFatGrams = 25.0,
                sugarGrams = 5.0,
                sodiumGrams = 600.0,
                proteinGrams = 45.0,
                saturatedFatGrams = 10.0,
                carbohydratesGrams = 35.0
            ),
            nSteps = 5,
            steps = listOf("Brown beef", "Simmer", "Add vegetables"),
            description = "Comfort food for winter",
            ingredients = listOf("beef", "potatoes", "carrots"),
            nIngredients = 3
        ),
        Meal(
            name = "Triple Fire Chicken Sandwich",
            id = 65,
            minutes = 15,
            contributorId = 989,
            submitted = LocalDate.parse("2025-01-30"),
            tags = listOf("spicy", "bowl"),
            nutrition = Nutrition(
                caloriesKcal = 300.0,
                totalFatGrams = 10.0,
                sugarGrams = 3.0,
                sodiumGrams = 250.0,
                proteinGrams = 30.0,
                saturatedFatGrams = 2.0,
                carbohydratesGrams = 20.0
            ),
            nSteps = 3,
            steps = listOf("chicken", "Add chili sauce", "Serve"),
            description = "A spicy and flavorful meal",
            ingredients = listOf("chicken", "chili", "rice"),
            nIngredients = 3
        )

    )

    @Test
    fun `should return meals that contain the keyword`() {
        //Given
        val query = "Chicken"
        every { mealRepository.getAllMeals() } returns fakeMeals

        //When
        val result = useCase.getMealByName(query)

        //Then
        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "Spicy Chili Chicken Bowl" })
        assertTrue(result.any { it.name == "Triple Fire Chicken Sandwich" })

    }

    @Test
    fun `should return meals that contain the word, regardless of the case`() {
        //Given
        val query = "cHiLi"
        every { mealRepository.getAllMeals() } returns fakeMeals

        //When
        val result = useCase.getMealByName(query)

        //Then
        assertEquals(1, result.size)
        assertEquals("Spicy Chili Chicken Bowl", result.first().name)
    }

    @Test
    fun `should return empty list if meal name does not match`() {
        //Given
        val query = "Kosharii"
        every { mealRepository.getAllMeals() } returns fakeMeals

        //When
        val result = useCase.getMealByName(query)

        //Then
        assertTrue(result.isEmpty())
    }
    //endregion

    //region get meal by country
    @Test
    fun getMealByCountry() {
    }
//endregion

    //region get iraqi meals
    @Test
    fun getIraqiMeals() {
    }
    //endregion
}