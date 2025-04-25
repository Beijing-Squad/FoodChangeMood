package logic.usecases

import com.google.common.truth.Truth.assertThat
import fake.createMeal
import io.mockk.every
import io.mockk.mockk
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsViewUseCase
import org.beijing.model.Nutrition
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ManageMealsViewUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: ManageMealsViewUseCase

    @BeforeEach
    fun setup() {
        mealRepository = mockk()
        useCase = ManageMealsViewUseCase(mealRepository)
    }

    //region get healthy quick prepared meals

    @Test
    fun `getHealthyQuickPreparedMeals should return empty list when no meals meet criteria`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(
            // All meals will make averages: fat=18.75, saturatedFat=9.375, carbs=30
            createMeal(
                name = "Slow Meal",  // Fails time (30 > 15)
                minutes = 30,
                nutrition = Nutrition(0.0, 10.0, 0.0, 0.0, 0.0, 5.0, 20.0)
            ),
            createMeal(
                name = "High Fat",  // Fats fat (20 > 18.75 avg)
                minutes = 10,
                nutrition = Nutrition(0.0, 20.0, 0.0, 0.0, 0.0, 5.0, 20.0)
            ),
            createMeal(
                name = "High Saturated Fat",  // Fails saturated fat (15 > 9.375 avg)
                minutes = 10,
                nutrition = Nutrition(0.0, 10.0, 0.0, 0.0, 0.0, 15.0, 20.0)
            ),
            createMeal(
                name = "High Carb",  // Fails carbs (60 > 30 avg)
                minutes = 10,
                nutrition = Nutrition(0.0, 10.0, 0.0, 0.0, 0.0, 5.0, 60.0)
            ),
            createMeal(
                name = "Slow High Fat",  // Fails time AND fat
                minutes = 20,
                nutrition = Nutrition(0.0, 25.0, 0.0, 0.0, 0.0, 5.0, 20.0)
            ),
            createMeal(
                name = "High Everything",  // Fails all nutrition checks
                minutes = 10,
                nutrition = Nutrition(0.0, 30.0, 0.0, 0.0, 0.0, 20.0, 50.0)
            ),
            createMeal(
                name = "Barely Slow",  // Fails time (16 > 15)
                minutes = 16,
                nutrition = Nutrition(0.0, 10.0, 0.0, 0.0, 0.0, 5.0, 20.0)
            ),
            createMeal(
                name = "Fat Bomb",  // Fails fat and saturated fat
                minutes = 10,
                nutrition = Nutrition(0.0, 25.0, 0.0, 0.0, 0.0, 15.0, 10.0)
            )
        )

        // When
        val result = useCase.getHealthyQuickPreparedMeals()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getHealthyQuickPreparedMeals should return meals that meet all criteria`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(
                name = "Quick Salad",
                minutes = 10,
                nutrition = Nutrition(
                    0.0,
                    5.0,
                    0.0,
                    0.0,
                    0.0,
                    2.0,
                    10.0
                )
            ),
            createMeal(
                name = "Fast Food Burger",
                minutes = 10,
                nutrition = Nutrition(
                    0.0,
                    20.0,
                    0.0,
                    0.0,
                    0.0,
                    10.0,
                    30.0
                )
            )
        )

        // When
        val result = useCase.getHealthyQuickPreparedMeals()

        // Then
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].name).isEqualTo("Quick Salad")
    }

    @Test
    fun `should exclude when saturated fat equals average`() {
        // Given
        val meals = listOf(
            createMeal(
                name = "meal with saturated fat equals average",
                nutrition = Nutrition(
                    0.0,
                    2.0,
                    0.0,
                    0.0,
                    0.0,
                    5.0,
                    5.0
                ),
                minutes = 10
            ),
            createMeal(
                name = "meal with the same",
                nutrition = Nutrition(
                    0.0,
                    4.0,
                    0.0,
                    0.0,
                    0.0,
                    5.0,
                    10.0
                ),
                minutes = 10
            )
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.getHealthyQuickPreparedMeals()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should exclude when carbs equal average`() {
        // Given
        val meals = listOf(
            createMeal(
                name = "unValid Meal with carbs= carbAvg",
                nutrition = Nutrition(
                    0.0,
                    2.0,
                    0.0,
                    0.0,
                    0.0,
                    2.0,
                    20.0
                ),
                minutes = 10
            ),
            createMeal(
                name = "Avg Carbs",
                nutrition = Nutrition(
                    0.0,
                    4.0,
                    0.0,
                    0.0,
                    0.0,
                    3.0,
                    20.0
                ),
                minutes = 10
            )
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.getHealthyQuickPreparedMeals()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should exclude when carbs above average`() {
        // Arrange - totalFatGrams (4.0) < avg (5.0), carbsGrams (21.0) > avg (20.0)
        val meals = listOf(
            createMeal(
                name = "low Carbs",
                nutrition = Nutrition(0.0, 4.0, 0.0, 0.0, 0.0, 4.0, 19.0),
                minutes = 10
            ),
            createMeal(
                name = "High Carbs",
                nutrition = Nutrition(0.0, 4.0, 0.0, 0.0, 0.0, 2.0, 21.0),
                minutes = 10
            )
        )
        every { mealRepository.getAllMeals() } returns meals

        // Act
        val result = useCase.getHealthyQuickPreparedMeals()

        // Assert
        assertThat(result).isEmpty()
    }

    @Test
    fun `getHealthyQuickPreparedMeals should return empty list when passed empty meals list`() {
        // Arrange
        every { mealRepository.getAllMeals() } returns emptyList()

        // Act
        val result = useCase.getHealthyQuickPreparedMeals()

        // Assert
        assertThat(result).isEmpty()
    }

    //endregion

    //region get sorted seafood by protein
    @Test
    fun getSortedSeaFoodByProtein() {
    }
    //endregion
}