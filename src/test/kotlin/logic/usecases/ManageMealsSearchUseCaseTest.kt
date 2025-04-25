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
    fun getMealByCountry() {
    }
    //endregion

    //region get iraqi meals
    @Test
    fun `getIraqiMeals should return meals with Iraqi tag or description`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(
                name = "Masgouf",
                id = 1,
                tags = listOf("Iraqi", "Grilled"),
                description = "Traditional Iraqi grilled fish"
            ),
            createMeal(
                name = "Kebab",
                id = 2,
                tags = listOf("Middle Eastern"),
                description = "Popular in Iraq"
            ),
            createMeal(
                name = "Pizza",
                id = 3,
                tags = listOf("Italian"),
                description = "Italian classic dish"
            )
        )

        // When
        val iraqiMeals = useCase.getIraqiMeals()

        // Then
        assertThat(iraqiMeals).hasSize(2)
        assertThat(iraqiMeals.map { it.name }).containsExactly("Masgouf", "Kebab")
    }

    @Test
    fun `getIraqiMeals should return empty list when no Iraqi meals exist`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(
                name = "Sushi",
                id = 1,
                tags = listOf("Japanese"),
                description = "Japanese delicacy"
            ),
            createMeal(
                name = "Pasta",
                id = 2,
                tags = listOf("Italian"),
                description = "Italian classic"
            )
        )

        // When
        val iraqiMeals = useCase.getIraqiMeals()

        // Then
        assertThat(iraqiMeals).isEmpty()
    }

    @Test
    fun `getIraqiMeals should match Iraqi tag case insensitively`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(
                name = "Dolma",
                id = 1,
                tags = listOf("iraqi", "vegetarian"),
                description = "Stuffed vegetables"
            ),
            createMeal(
                name = "Biryani",
                id = 2,
                tags = listOf("Indian"),
                description = "Spiced rice dish"
            )
        )

        // When
        val iraqiMeals = useCase.getIraqiMeals()

        // Then
        assertThat(iraqiMeals).hasSize(1)
        assertThat(iraqiMeals[0].name).isEqualTo("Dolma")
    }

    @Test
    fun `getIraqiMeals should match Iraqi in description case insensitively`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(
                name = "Tashreeb",
                id = 1,
                tags = listOf("Middle Eastern"),
                description = "An iraqi bread soup dish"
            ),
            createMeal(
                name = "Falafel",
                id = 2,
                tags = listOf("Middle Eastern"),
                description = "Popular across the region"
            )
        )

        // When
        val iraqiMeals = useCase.getIraqiMeals()

        // Then
        assertThat(iraqiMeals).hasSize(1)
        assertThat(iraqiMeals[0].name).isEqualTo("Tashreeb")
    }
    //endregion
}