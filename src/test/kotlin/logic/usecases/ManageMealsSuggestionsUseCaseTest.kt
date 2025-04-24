package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import mealsWithoutPotato
import nineMealsContainsPotato
import mealsContainsPotato
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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

    //region suggest ten random meals contains potato test
    @Test
    fun `should return meals containing potato when have it`() {
        //given
        every { mealRepository.getAllMeals() } returns mealsContainsPotato

        //when
        val result = useCase.suggestTenRandomMealsContainsPotato()

        //then
        result.forEach { meal ->
            assertThat(meal.ingredients).contains("potato")
        }

    }

    @Test
    fun `should throw exception when meals does not contains potato`() {
        //given
        every { mealRepository.getAllMeals() } returns mealsWithoutPotato

        //when & then
        assertThrows<IllegalArgumentException> {
            useCase.suggestTenRandomMealsContainsPotato()
        }
    }

    @Test
    fun `should return ten random meals when passed meals contains potato`() {
        //given
        every { mealRepository.getAllMeals() } returns mealsContainsPotato

        //when
        val result = useCase.suggestTenRandomMealsContainsPotato()

        //then
        assertThat(result.size).isEqualTo(10)

    }

    @Test
    fun `should throw exception when return empty list or smaller than ten meals contains potato`(){
        //given
        every { mealRepository.getAllMeals() } returns nineMealsContainsPotato

        //when && then
        assertThrows<IllegalArgumentException>{
            useCase.suggestTenRandomMealsContainsPotato()
        }
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
    fun suggestMealHaveMoreThanSevenHundredCalories() {
    }
//endregion

    //region check meal calories content
    @Test
    fun checkMealCaloriesContent() {
    }
    //endregion
}