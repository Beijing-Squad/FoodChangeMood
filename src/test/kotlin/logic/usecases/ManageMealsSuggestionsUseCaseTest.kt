package logic.usecases

import com.google.common.truth.Truth.assertThat
import fake.createMeal
import io.mockk.every
import io.mockk.mockk
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.model.Meal
import org.beijing.model.Nutrition
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import kotlin.test.assertEquals

class ManageMealsSuggestionsUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: ManageMealsSuggestionsUseCase
    private val usedMealIds = mutableSetOf<Int>()

    @BeforeEach
    fun setUp() {
        mealRepository = mockk(relaxed = true)
        useCase = ManageMealsSuggestionsUseCase(mealRepository)
        usedMealIds.clear()
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

    @Test
    fun `should return a keto meal when valid meals are available`() {
        // Given
        val maxCarbs = 20
        val ketoMeal = createMeal(
            id = 1,
            nutrition = Nutrition(
                caloriesKcal = 500.0,
                totalFatGrams = 40.0,
                sugarGrams = 2.0,
                sodiumGrams = 1.0,
                proteinGrams = 20.0,
                saturatedFatGrams = 15.0,
                carbohydratesGrams = 10.0
            )
        )
        every { mealRepository.getAllMeals() } returns listOf(ketoMeal)

        // When
        val result = useCase.suggestKetoMeal(usedMealIds)

        // Then
        assertNotNull(result)
        assertEquals(ketoMeal, result)
        assertThat(result.nutrition.carbohydratesGrams).isLessThan(maxCarbs)
        assertThat(result.nutrition.totalFatGrams > result.nutrition.proteinGrams)
        assertThat(usedMealIds.contains(ketoMeal.id))

    }

    @Test
    fun `should return null and display suitable message when no valid keto meals are available`() {
        // Given
        val nonKetoMeal = createMeal(
            id = 2,
            nutrition = Nutrition(
                caloriesKcal = 500.0,
                totalFatGrams = 10.0,
                sugarGrams = 2.0,
                sodiumGrams = 1.0,
                proteinGrams = 20.0,
                saturatedFatGrams = 5.0,
                carbohydratesGrams = 50.0
            )
        )
        every { mealRepository.getAllMeals() } returns listOf(nonKetoMeal)

        // When
        val result = useCase.suggestKetoMeal(usedMealIds)

        // Then
        assertNull(result)
        assertThat("\uD83D\uDE14 No more keto meals to suggest.")
        assertThat(usedMealIds.isEmpty())
    }

    @Test
    fun `should return suitable message when all keto meals are already used`() {
        // Given
        val ketoMeal = createMeal(
            id = 3,
            nutrition = Nutrition(
                caloriesKcal = 500.0,
                totalFatGrams = 40.0,
                sugarGrams = 2.0,
                sodiumGrams = 1.0,
                proteinGrams = 20.0,
                saturatedFatGrams = 15.0,
                carbohydratesGrams = 10.0
            )
        )
        usedMealIds.add(ketoMeal.id)
        every { mealRepository.getAllMeals() } returns listOf(ketoMeal)

        // When
        val result = useCase.suggestKetoMeal(usedMealIds)

        // Then
        assertNull(result)
        assertThat("\uD83D\uDE14 No more keto meals to suggest.")
        assertThat(usedMealIds).containsExactly(ketoMeal.id)
    }

    @Test
    fun `should return a random valid keto meal when multiple valid options available`() {
        // Given
        val maxCarbs = 20
        val ketoMeal1 = createMeal(
            id = 4,
            nutrition = Nutrition(
                caloriesKcal = 500.0,
                totalFatGrams = 40.0,
                sugarGrams = 2.0,
                sodiumGrams = 1.0,
                proteinGrams = 20.0,
                saturatedFatGrams = 15.0,
                carbohydratesGrams = 10.0
            )
        )
        val ketoMeal2 = createMeal(
            id = 5,
            nutrition = Nutrition(
                caloriesKcal = 600.0,
                totalFatGrams = 50.0,
                sugarGrams = 3.0,
                sodiumGrams = 1.5,
                proteinGrams = 25.0,
                saturatedFatGrams = 20.0,
                carbohydratesGrams = 15.0
            )
        )
        every { mealRepository.getAllMeals() } returns listOf(ketoMeal1, ketoMeal2)

        // When
        val results = mutableSetOf<Meal>()
        repeat(10) {
            usedMealIds.clear()
            val result = useCase.suggestKetoMeal(usedMealIds)
            if (result != null) results.add(result)
        }

        // Then
        assertThat(results).containsAnyOf(ketoMeal1, ketoMeal2)
        assertThat(results.all {
            it.nutrition.carbohydratesGrams < maxCarbs &&
                    it.nutrition.totalFatGrams > it.nutrition.proteinGrams
        })
    }

    @Test
    fun `should not return meals with high carbs or low fat compared to protein`() {
        // Given
        val highCarbMeal = createMeal(
            id = 1,
            nutrition = Nutrition(
                caloriesKcal = 500.0,
                totalFatGrams = 40.0,
                sugarGrams = 2.0,
                sodiumGrams = 1.0,
                proteinGrams = 20.0,
                saturatedFatGrams = 15.0,
                carbohydratesGrams = 30.0
            )
        )
        val lowFatMeal = createMeal(
            id = 2,
            nutrition = Nutrition(
                caloriesKcal = 500.0,
                totalFatGrams = 15.0,
                sugarGrams = 2.0,
                sodiumGrams = 1.0,
                proteinGrams = 20.0,
                saturatedFatGrams = 5.0,
                carbohydratesGrams = 10.0
            )
        )
        every { mealRepository.getAllMeals() } returns listOf(highCarbMeal, lowFatMeal)

        // When
        val result = useCase.suggestKetoMeal(usedMealIds)

        // Then
        assertNull(result)
        assertThat(usedMealIds.isEmpty())
    }

    @Test
    fun `should return suitable message when meal list is empty`() {
        // Given
        every { mealRepository.getAllMeals() } returns emptyList()

        // When
        val result = useCase.suggestKetoMeal(usedMealIds)

        // Then
        assertNull(result)
        assertThat("\uD83D\uDE14 No more keto meals to suggest.")
        assertThat(usedMealIds.isEmpty())
    }

    @Test
    fun `should add returned meal ID to usedMealIds`() {
        // Given
        val ketoMeal = createMeal(
            id = 1,
            nutrition = Nutrition(
                caloriesKcal = 500.0,
                totalFatGrams = 40.0,
                sugarGrams = 2.0,
                sodiumGrams = 1.0,
                proteinGrams = 20.0,
                saturatedFatGrams = 15.0,
                carbohydratesGrams = 10.0
            )
        )
        every { mealRepository.getAllMeals() } returns listOf(ketoMeal)

        // When
        val result = useCase.suggestKetoMeal(usedMealIds)

        // Then
        assertNotNull(result)
        assertEquals(ketoMeal.id, result.id)
        assertThat(usedMealIds).containsExactly(ketoMeal.id)
    }

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