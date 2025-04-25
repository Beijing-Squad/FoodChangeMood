package logic.usecases

import com.google.common.truth.Truth.assertThat
import fake.createMeal
import fake.mealsListWithHighCaloriesMeals
import fake.mealsWithHighCalories
import fake.mealsWithNoHighCalories
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
//region keto meal
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
    fun `should return null and display suitable message when all keto meals are already used`() {
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
    fun `should add returned meal ID to usedMealIds when it used`() {
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
//endregion
    // easy meal region
    @Test
    fun `should return sorted easy meals based on criteria`() {
        // Given
        val meals = listOf(
            createMeal(id = 1, name = "Simple Salad", tags = listOf("Healthy"), minutes = 10, nSteps = 3, nIngredients = 4, contributorId = 1),
            createMeal(id = 2, name = "Quick Pasta", tags = listOf("Italian"), minutes = 15, nSteps = 4, nIngredients = 5, contributorId = 2),
            createMeal(id = 3, name = "Fruit Bowl", tags = listOf("Healthy"), minutes = 5, nSteps = 2, nIngredients = 3, contributorId = 3),
            createMeal(id = 4, name = "Rice Dish", tags = listOf("Iraqi"), minutes = 20, nSteps = 5, nIngredients = 7, contributorId = 4)
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.suggestEasyPreparedMeal()

        // Then
        assert(result.size == 3)
        assert(result[0].name == "Fruit Bowl")
        assert(result[1].name == "Simple Salad")
        assert(result[2].name == "Quick Pasta")
    }

    @Test
    fun `should return empty list when no meals meet easy meal criteria`() {
        // Given
        val meals = listOf(
            createMeal(id = 1, name = "Hard Meal", tags = listOf("Complex"), minutes = 60, nSteps = 10, nIngredients = 12, contributorId = 1)
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.suggestEasyPreparedMeal()

        // Then
        assert(result.isEmpty())
    }

    @Test
    fun `should return only meals with valid steps, ingredients, and time limit`() {
        // Given
        val meals = listOf(
            createMeal(id = 1, name = "Easy Breakfast", tags = listOf("Breakfast"), minutes = 15, nSteps = 3, nIngredients = 4, contributorId = 1),
            createMeal(id = 2, name = "Gourmet Meal", tags = listOf("Dinner"), minutes = 40, nSteps = 7, nIngredients = 10, contributorId = 2)
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.suggestEasyPreparedMeal()

        // Then
        assert(result.size == 1)
        assert(result[0].name == "Easy Breakfast")
    }

    @Test
    fun `should handle edge case with exactly matching meal count`() {
        // Given
        val meals = listOf(
            createMeal(id = 1, name = "Breakfast Smoothie", tags = listOf("Smoothie"), minutes = 5, nSteps = 2, nIngredients = 3, contributorId = 1),
            createMeal(id = 2, name = "Light Salad", tags = listOf("Salad"), minutes = 10, nSteps = 3, nIngredients = 4, contributorId = 2),
            createMeal(id = 3, name = "Quick Wrap", tags = listOf("Wrap"), minutes = 10, nSteps = 3, nIngredients = 5, contributorId = 3)
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.suggestEasyPreparedMeal()

        // Then
        assert(result.size == 3)
        assert(result[0].name == "Breakfast Smoothie")
        assert(result[1].name == "Light Salad")
        assert(result[2].name == "Quick Wrap")
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