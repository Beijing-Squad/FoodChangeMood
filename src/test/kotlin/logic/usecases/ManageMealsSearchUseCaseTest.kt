package logic.usecases

import com.google.common.truth.Truth.assertThat
import fake.createMeal
import fake.meals
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsSearchUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

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
    fun `should return list of meals when date is valid`() {
        // Given
        val date = "2025-03-10"
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(submitted = LocalDate(2025, 2, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 4, 10)),
        )

        // When
        val result = useCase.getMealsByDate(date)

        // Then
        assert(result.size == 2)
    }

    @Test
    fun `should throw exception when no meals found on the date`() {
        // Given
        val date = "2025-01-01"
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(submitted = LocalDate(2025, 2, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 4, 10)),
        )

        // When && Then
        assertThrows<Exception> {
            useCase.getMealsByDate(date)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "2023 05 10",
        "2023@05@10",
        "20230615",
        "2023-600-01",
        "2023/01/02",
        "2023--32",
    )
    fun `should throw IllegalArgumentException when date is invalid`(date: String) {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(submitted = LocalDate(2025, 2, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 4, 10)),
        )

        // When && Then
        assertThrows<IllegalArgumentException> {
            useCase.getMealsByDate(date)
        }
    }

    @Test
    fun `should throw IllegalArgumentException when date is empty`() {
        // Given
        val date = ""
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(submitted = LocalDate(2025, 2, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 4, 10)),
        )

        // When && Then
        assertThrows<IllegalArgumentException> {
            useCase.getMealsByDate(date)
        }
    }
    //endregion

    //region get meal by id
    @Test
    fun `should return the matched meal when id is valid`() {
        // Given
        val id = 1
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(id = 1),
            createMeal(id = 2),
            createMeal(id = 3),
            createMeal(id = 4),
        )

        // When
        val result = useCase.getMealById(id)

        // Then
        assert(result.id == id)
    }

    @Test
    fun `should throw exception when no found meal for the id`() {
        // Given
        val id = 5
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(id = 1),
            createMeal(id = 2),
            createMeal(id = 3),
            createMeal(id = 4),
        )

        // When && Then
        assertThrows<Exception> {
            useCase.getMealById(id)
        }
    }
    //endregion

    //region gets gym helper
    @ParameterizedTest
    @CsvSource(
        "-1000.0, 50.0",
        "20.0,-1000.0"
    )
    fun `should throw exception when target calories or target protein is less than zero`(
        targetCalories: Double,
        targetProtein: Double
    ) {
        // Given
        every { mealRepository.getAllMeals() } returns meals

        // Then && When
        assertThrows<Exception> {
            useCase
                .getGymHelperMealsByCaloriesAndProtein(targetCalories, targetProtein)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "1500.0, 100.0",
        "300.0, 10200.0",
    )
    fun `should throw exception when no have gym helper meals`(
        targetCalories: Double,
        targetProtein: Double
    ) {
        // Given
        every { mealRepository.getAllMeals() } returns meals

        // Then && When
        assertThrows<Exception> {
            useCase
                .getGymHelperMealsByCaloriesAndProtein(targetCalories, targetProtein)
        }

    }

    @Test
    fun `should return gym helper meals when target calories and target protein are vaild`() {
        // Given
        val targetCalories = 250.0
        val targetProtein = 5.0
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase
            .getGymHelperMealsByCaloriesAndProtein(targetCalories, targetProtein)

        // Then
        assertThat(result.size).isEqualTo(2)

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
