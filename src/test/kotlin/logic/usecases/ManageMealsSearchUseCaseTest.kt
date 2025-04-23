package logic.usecases

import fake.mealsWithDate
import io.mockk.every
import io.mockk.mockk
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
    fun `should return list of meals when getMealsByDate called with valid date`() {
        // Given
        every { mealRepository.getAllMeals() } returns mealsWithDate

        // When
        val result = useCase.getMealsByDate("2023-05-10")

        // Then
        assert(result.size == 2)
    }

    @Test
    fun `should throw exception when no meals found on the date`() {
        // Given
        every { mealRepository.getAllMeals() } returns mealsWithDate

        // When && Then
        assertThrows<Exception> {
            useCase.getMealsByDate("2024-06-01")
        }
    }


    @ParameterizedTest
    @CsvSource(
        "2023 05 10",
        "2023@05@10",
        "20230615",
        "2023-60-01",
        "2023/01/02"
    )
    fun `should throw IllegalArgumentException when getMealsByDate called with invalid date`(date: String) {
        // Given
        every { mealRepository.getAllMeals() } returns mealsWithDate

        // When && Then
        assertThrows<IllegalArgumentException> {
            useCase.getMealsByDate(date)
        }
    }

    @Test
    fun `should throw exception when the date is greater than current date`() {
        // Given
        every { mealRepository.getAllMeals() } returns mealsWithDate

        // When && Then
        assertThrows<Exception> {
            useCase.getMealsByDate("2030-04-23")
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