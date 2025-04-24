package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.usecases.fakedata.mealsListWithHighCaloriesMeals
import logic.usecases.fakedata.mealsWithHighCalories
import logic.usecases.fakedata.mealsWithNoHighCalories
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ManageMealsSuggestionsUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: ManageMealsSuggestionsUseCase

    @BeforeEach
    fun setup() {
        mealRepository = mockk()
        useCase = ManageMealsSuggestionsUseCase(mealRepository)
    }

    //region suggest sweets with no eggs
    @Test
    fun suggestSweetsWithNoEggs() {
    }
//endregion

    //region suggest ten random meals contains potato
    @Test
    fun suggestTenRandomMealsContainsPotato() {
    }
//endregion

    //region suggest italian large group meals
    @Test
    fun suggestItalianLargeGroupsMeals() {
    }
//endregion

    //region suggest keto meal
    @Test
    fun suggestKetoMeal() {
    }
//endregion

    //region suggest easy prepared meal
    @Test
    fun suggestEasyPreparedMeal() {
    }
//endregion

    //region suggest meal have more than seven hundred calories
    @Test
    fun `should return meals with high calories When give list of meals`() {
        //Given
        every { mealRepository.getAllMeals() } returns mealsListWithHighCaloriesMeals
        //When
        val result = useCase.suggestMealHaveMoreThanSevenHundredCalories()
        //Then
        assertThat(result).containsExactlyElementsIn(mealsWithHighCalories)
    }

    @Test
    fun `should return empty list When give empty list`() {
        //Given
        every { mealRepository.getAllMeals() } returns listOf()
        //When
        val result = useCase.suggestMealHaveMoreThanSevenHundredCalories()
        //Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list When give list of meals have low calories`() {
        //Given
        every { mealRepository.getAllMeals() } returns mealsWithNoHighCalories
        //When
        val result = useCase.suggestMealHaveMoreThanSevenHundredCalories()
        //Then
        assertThat(result).isEmpty()
    }
    //endregion

    //region check meal calories content
    @Test
    fun checkMealCaloriesContent() {
    }
    //endregion
}