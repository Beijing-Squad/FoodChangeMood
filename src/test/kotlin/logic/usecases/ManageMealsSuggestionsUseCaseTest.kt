package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.model.Meal
import org.beijing.model.Nutrition
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import kotlin.test.assertEquals
import kotlinx.datetime.*
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
@Test
 fun suggestSweetsWithNoEggs() {}

@Test
 fun suggestTenRandomMealsContainsPotato() {}

@Test
 fun suggestItalianLargeGroupsMeals() {}

 @Test
 fun `should return a keto meal when suitable meals are available`() {
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
  every { mealRepository.getAllMeals()} returns listOf(ketoMeal)

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
 fun `should return null when no valid keto meals are available`() {
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
  every { mealRepository.getAllMeals()} returns listOf(nonKetoMeal)

  // When
  val result = useCase.suggestKetoMeal(usedMealIds)

  // Then
  assertNull(result)
  assertThat(usedMealIds.isEmpty())
 }

 @Test
 fun `should return null when all keto meals are already used`() {
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
  every { mealRepository.getAllMeals()} returns listOf(ketoMeal)

  // When
  val result = useCase.suggestKetoMeal(usedMealIds)

  // Then
  assertNull(result)
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
  every { mealRepository.getAllMeals()} returns listOf(ketoMeal1, ketoMeal2)

  // When
  val results = mutableSetOf<Meal>()
  repeat(10) {
   usedMealIds.clear()
   val result = useCase.suggestKetoMeal(usedMealIds)
   if (result != null) results.add(result)
  }

  // Then
  assertThat(results).containsAnyOf(ketoMeal1, ketoMeal2)
  assertThat(results.all { it.nutrition.carbohydratesGrams < maxCarbs &&
          it.nutrition.totalFatGrams > it.nutrition.proteinGrams })
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
  every { mealRepository.getAllMeals()} returns listOf(highCarbMeal, lowFatMeal)

  // When
  val result = useCase.suggestKetoMeal(usedMealIds)

  // Then
  assertNull(result)
  assertThat(usedMealIds.isEmpty())
 }

 @Test
 fun `should return null when meal list is empty`() {
  // Given
  every { mealRepository.getAllMeals()} returns emptyList()

  // When
  val result = useCase.suggestKetoMeal(usedMealIds)

  // Then
  assertNull(result)
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
  every { mealRepository.getAllMeals()} returns listOf(ketoMeal)

  // When
  val result = useCase.suggestKetoMeal(usedMealIds)

  // Then
  assertNotNull(result)
  assertEquals(ketoMeal.id, result.id)
  assertThat(usedMealIds).containsExactly(ketoMeal.id)
 }

 private fun createMeal(id: Int, nutrition: Nutrition): Meal {
  return Meal(
   name = "Test Meal $id",
   id = id,
   minutes = 30,
   contributorId = 1,
   submitted = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
   tags = emptyList(),
   nutrition = nutrition,
   nSteps = 5,
   steps = listOf("Step 1", "Step 2"),
   description = "Test description",
   ingredients = listOf("Ingredient 1", "Ingredient 2"),
   nIngredients = 2
  )
 }


@Test
 fun suggestEasyPreparedMeal() {}

@Test
 fun suggestMealHaveMoreThanSevenHundredCalories() {}

@Test
 fun checkMealCaloriesContent() {}
}