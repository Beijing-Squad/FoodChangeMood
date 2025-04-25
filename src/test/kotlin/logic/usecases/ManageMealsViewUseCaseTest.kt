package logic.usecases

import com.google.common.truth.Truth.assertThat
import fake.createMeal
import fake.mealsListWithSeaFood
import fake.mealsWithNoSeaFood
import fake.seafoodMealOrders
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
            createMeal(
                name = "Slow Meal",
                minutes = 30,
                nutrition = Nutrition(0.0, 10.0, 0.0, 0.0, 0.0, 5.0, 20.0)
            ),
            createMeal(
                name = "High Fat",
                minutes = 10,
                nutrition = Nutrition(0.0, 20.0, 0.0, 0.0, 0.0, 5.0, 20.0)
            ),
            createMeal(
                name = "High Saturated Fat",
                minutes = 10,
                nutrition = Nutrition(0.0, 10.0, 0.0, 0.0, 0.0, 15.0, 20.0)
            ),
            createMeal(
                name = "High Carb",
                minutes = 10,
                nutrition = Nutrition(0.0, 10.0, 0.0, 0.0, 0.0, 5.0, 60.0)
            ),
            createMeal(
                name = "Slow High Fat",
                minutes = 20,
                nutrition = Nutrition(0.0, 25.0, 0.0, 0.0, 0.0, 5.0, 20.0)
            ),
            createMeal(
                name = "High Everything",
                minutes = 10,
                nutrition = Nutrition(0.0, 30.0, 0.0, 0.0, 0.0, 20.0, 50.0)
            ),
            createMeal(
                name = "Barely Slow",
                minutes = 16,
                nutrition = Nutrition(0.0, 10.0, 0.0, 0.0, 0.0, 5.0, 20.0)
            ),
            createMeal(
                name = "Fat Bomb",
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
                    0.0, 5.0, 0.0, 0.0, 0.0, 2.0, 10.0
                )
            ),
            createMeal(
                name = "Fast Food Burger",
                minutes = 10,
                nutrition = Nutrition(
                    0.0, 20.0, 0.0, 0.0, 0.0, 10.0, 30.0
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
                minutes = 10,
                nutrition = Nutrition(
                    0.0, 2.0, 0.0, 0.0, 0.0, 5.0, 5.0
                )
            ),
            createMeal(
                name = "meal with the same",
                minutes = 10,
                nutrition = Nutrition(
                    0.0, 4.0, 0.0, 0.0, 0.0, 5.0, 10.0
                )
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
                minutes = 10,
                nutrition = Nutrition(
                    0.0, 2.0, 0.0, 0.0, 0.0, 2.0, 20.0
                )
            ),
            createMeal(
                name = "Avg Carbs",
                minutes = 10,
                nutrition = Nutrition(
                    0.0, 4.0, 0.0, 0.0, 0.0, 3.0, 20.0
                )
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
        // Given
        val meals = listOf(
            createMeal(
                name = "low Carbs",
                minutes = 10,
                nutrition = Nutrition(
                    0.0, 4.0, 0.0, 0.0, 0.0, 4.0, 19.0
                )
            ),
            createMeal(
                name = "High Carbs",
                minutes = 10,
                nutrition = Nutrition(
                    0.0, 4.0, 0.0, 0.0, 0.0, 2.0, 21.0
                )
            )
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.getHealthyQuickPreparedMeals()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getHealthyQuickPreparedMeals should return empty list when passed empty meals list`() {
        // Given
        every { mealRepository.getAllMeals() } returns emptyList()

        // When
        val result = useCase.getHealthyQuickPreparedMeals()

        // Then
        assertThat(result).isEmpty()
    }

    //endregion

    //region get sorted seafood by protein
    @Test
    fun `should return sorted seaFood meals ordered by protein When give list of meals`() {
        //Given
        every { mealRepository.getAllMeals() } returns mealsListWithSeaFood
        //When
        val result = useCase.getSortedSeaFoodByProtein()
        //Then
        assertThat(result).containsExactlyElementsIn(seafoodMealOrders).inOrder()
    }

    @Test
    fun `should return empty list When give empty list`() {
        //Given
        every { mealRepository.getAllMeals() } returns listOf()
        //When
        val result = useCase.getSortedSeaFoodByProtein()
        //Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list When give list of meals have no seafood meals`() {
        //Given
        every { mealRepository.getAllMeals() } returns mealsWithNoSeaFood
        //When
        val result = useCase.getSortedSeaFoodByProtein()
        //Then
        assertThat(result).isEmpty()
    }
    //endregion
}