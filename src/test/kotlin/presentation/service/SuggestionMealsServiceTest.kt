package org.beijing.presentation.service

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.model.Meal
import org.beijing.model.Nutrition
import org.beijing.presentation.ViewMealDetails
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.mp.KoinPlatform
import kotlin.test.assertTrue

class SuggestionMealsServiceTest {

    private lateinit var suggestionMealsUseCase: ManageMealsSuggestionsUseCase
    private lateinit var viewMealDetails: ViewMealDetails
    private lateinit var suggestionMealsService: SuggestionMealsService

    @BeforeEach
    fun setUp() {
        suggestionMealsUseCase = mockk(relaxed = true)
        viewMealDetails = mockk(relaxed = true)
        // Mock Koin dependency injection
        mockkObject(KoinPlatform)
        every { KoinPlatform.getKoin().get<ManageMealsSuggestionsUseCase>() } returns suggestionMealsUseCase
        every { KoinPlatform.getKoin().get<ViewMealDetails>() } returns viewMealDetails
        suggestionMealsService = SuggestionMealsService()
    }

    @AfterEach
    fun clearMockk() {
        unmockkAll()
    }

    @Test
    fun `should display keto meal and call displayMealDetails when user inputs yes`() {
        // Given
        val ketoMeal = createKetoMeal(id = 1, name = "Keto Meal 1")
        val usedKetoMealIds = mutableSetOf<Int>()
        every { suggestionMealsUseCase.suggestKetoMeal(usedKetoMealIds) } returns ketoMeal
        every { viewMealDetails.displayMealDetails(ketoMeal) } just Runs
        mockUserInput("yes")

        // When
        val consoleOutput = captureConsoleOutput {
            suggestionMealsService.launchKetoMealHelper()
        }

        // Then
        assertThat(consoleOutput.contains("\n🥑 Keto Meal: ${ketoMeal.name}"))
        assertThat(consoleOutput.contains("Short Description: ${ketoMeal.description}"))
        assertThat(consoleOutput.contains("Do you like it? ❤"))
        verify(exactly = 1) { viewMealDetails.displayMealDetails(ketoMeal) }
        verifyValidKeto(ketoMeal)
    }

    @Test
    fun `should display another keto meal when user inputs no`() {
        // Given
        val ketoMeal1 = createKetoMeal(id = 1, name = "Keto Meal 1")
        val ketoMeal2 = createKetoMeal(id = 2, name = "Keto Meal 2")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returnsMany listOf(ketoMeal1, ketoMeal2)
        mockUserInput("no", "exit")

        // When
        val consoleOutput = captureConsoleOutput {
            suggestionMealsService.launchKetoMealHelper()
        }

        // Then
        assertThat(consoleOutput.contains("🥑 Keto Meal: Keto Meal 1"))
        assertThat(consoleOutput.contains("Do you like it? ❤"))
        assertThat(consoleOutput.contains("🔄 Okay! Let's try another one."))
        assertThat(consoleOutput.contains("🥑 Keto Meal: Keto Meal 2"))
        verify(exactly = 0) { viewMealDetails.displayMealDetails(any()) }
        verifyValidKeto(ketoMeal1)
        verifyValidKeto(ketoMeal2)
    }

    @Test
    fun `should exit loop when user inputs exit`() {
        // Given
        val ketoMeal = createKetoMeal(id = 1, name = "Keto Meal")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        mockUserInput("exit")

        // When
        val consoleOutput = captureConsoleOutput {
            suggestionMealsService.launchKetoMealHelper()
        }

        // Then
        assertTrue(consoleOutput.contains("🥑 Keto Meal: Keto Meal"))
        assertTrue(consoleOutput.contains("Do you like it? ❤"))
        verify(exactly = 0) { viewMealDetails.displayMealDetails(any()) }
        verifyValidKeto(ketoMeal)
    }

    @Test
    fun `should display error message when invalid input and continue loop`() {
        // Given
        val ketoMeal = createKetoMeal(id = 1, name = "Keto Meal 1")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        mockUserInput("invalid", "exit")

        // When
        val consoleOutput = captureConsoleOutput {
            suggestionMealsService.launchKetoMealHelper()
        }

        // Then
        assertTrue(consoleOutput.contains("🥑 Keto Meal: Keto Meal 1"))
        assertTrue(consoleOutput.contains("Do you like it? ❤"))
        assertTrue(consoleOutput.contains("⚠️ Please type 'yes' or 'no'"))
        verify(exactly = 0) { viewMealDetails.displayMealDetails(any()) }
        verifyValidKeto(ketoMeal)
    }

    @Test
    fun `should display no more meals message when no keto meals are available`() {
        // Given
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns null

        // When
        val consoleOutput = captureConsoleOutput {
            suggestionMealsService.launchKetoMealHelper()
        }

        // Then
        assertThat(consoleOutput.contains("😔 No more keto meals to suggest."))
        verify(exactly = 0) { viewMealDetails.displayMealDetails(any()) }
    }

    private fun createKetoMeal(id: Int, name: String): Meal {
        return Meal(
            name = name,
            id = id,
            minutes = 30,
            contributorId = 1,
            submitted = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
            tags = listOf("keto"),
            nutrition = Nutrition(
                caloriesKcal = 500.0,
                totalFatGrams = 40.0, // High fat
                sugarGrams = 2.0,
                sodiumGrams = 1.0,
                proteinGrams = 20.0, // Lower than fat
                saturatedFatGrams = 15.0,
                carbohydratesGrams = 10.0 // Low carbs (< 20)
            ),
            nSteps = 5,
            steps = listOf("Step 1", "Step 2"),
            description = "A keto-friendly meal",
            ingredients = listOf("Ingredient 1", "Ingredient 2"),
            nIngredients = 2
        )
    }

    private fun verifyValidKeto(meal: Meal) {
        val maxCarbs = 20
        assertTrue("Carbohydrates should be less than $maxCarbs grams") {
            meal.nutrition.carbohydratesGrams < maxCarbs
        }
        assertTrue("Total fat should be greater than protein") {
            meal.nutrition.totalFatGrams > meal.nutrition.proteinGrams
        }
    }

    private fun mockUserInput(vararg inputs: String) {
        val iterator = inputs.iterator()
        mockkStatic("")
        every { readlnOrNull() } answers { if (iterator.hasNext()) iterator.next() else null }
    }

    private fun captureConsoleOutput(block: () -> Unit): String {
        val outputStream = java.io.ByteArrayOutputStream()
        System.setOut(java.io.PrintStream(outputStream))
        try {
            block()
            return outputStream.toString()
        } finally {
            System.setOut(System.out)
        }
    }
}