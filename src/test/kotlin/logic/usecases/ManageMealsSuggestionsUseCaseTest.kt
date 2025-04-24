package logic.usecases

import helper.mealWithTags
import io.mockk.every
import io.mockk.mockk
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.junit.jupiter.api.Assertions
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
    fun `should Return Meals With Both Tags when Meals Contain Italian And Large Group Tags`() {
        val meals = listOf(
            mealWithTags(1, listOf("italian", "for-large-groups")),
            mealWithTags(2, listOf("italian")),
            mealWithTags(3, listOf("for-large-groups")),
            mealWithTags(4, listOf("dessert"))
        )

        every { mealRepository.getAllMeals() } returns meals

        val result = useCase.suggestItalianLargeGroupsMeals()

        assert(result.size == 1&&result.first().id==1)
    }
    @Test
    fun `should Return EmptyList when No Meal Matches Both Tags`() {
        val meals = listOf(
            mealWithTags(1, listOf("italian")),
            mealWithTags(2, listOf("for-large-groups")),
            mealWithTags(3, listOf("dessert"))
        )

        every { mealRepository.getAllMeals() } returns meals

        val result = useCase.suggestItalianLargeGroupsMeals()

        Assertions.assertTrue(result.isEmpty())
    }
    @Test
    fun `should Match Tags Case Insensitively when Tags Have Different Cases`() {
        val meals = listOf(
            mealWithTags(1, listOf("ITALIAN", "FOR-LARGE-GROUPS")),
            mealWithTags(2, listOf("Italian", "For-Large-Groups")),
            mealWithTags(3, listOf("ITALIAN")),
            mealWithTags(4, listOf("for-large-groups"))
        )

        every { mealRepository.getAllMeals() } returns meals

        val result = useCase.suggestItalianLargeGroupsMeals()

        Assertions.assertEquals(2, result.size)
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