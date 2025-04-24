package logic.usecases


import helper.createMeal
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
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(submitted = LocalDate(2025, 2, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 4, 10)),
        )

        // When
        val result = useCase.getMealsByDate("2025-03-10")

        // Then
        assert(result.size == 2)
    }

    @Test
    fun `should throw exception when no meals found on the date`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(submitted = LocalDate(2025, 2, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 4, 10)),
        )

        // When && Then
        assertThrows<Exception> {
            useCase.getMealsByDate("2025-01-01")
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
    fun getMealByCountry() {
    }
    //endregion

    //region get iraqi meals
    @Test
    fun getIraqiMeals() {
    }
    //endregion
}