package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.fakeData.meals
import logic.fakeData.mealsWithNoSeaFood
import logic.fakeData.seafoodMealOrders
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsViewUseCase
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
    fun getHealthyQuickPreparedMeals() {
    }
//endregion

    //region get sorted seafood by protein
    @Test
    fun `should return sorted seaFood meals ordered by protein When give list of meals`() {
        //Given
        every { mealRepository.getAllMeals() } returns meals
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