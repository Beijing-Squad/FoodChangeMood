package presentation.service

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.datetime.LocalDate
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.model.Meal
import org.beijing.model.Nutrition
import org.beijing.presentation.ViewMealDetails
import org.beijing.presentation.service.SuggestionMealsService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.mp.KoinPlatform

class SuggestionMealsServiceTest {

    private lateinit var suggestionMealsUseCase: ManageMealsSuggestionsUseCase
    private lateinit var viewMealDetails: ViewMealDetails
    private lateinit var suggestionMealsService: SuggestionMealsService
    private val outputStream = java.io.ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        suggestionMealsUseCase = mockk(relaxed = true)
        viewMealDetails = mockk(relaxed = true)
        mockkObject(KoinPlatform)
        every { KoinPlatform.getKoin().get<ManageMealsSuggestionsUseCase>() } returns suggestionMealsUseCase
        every { KoinPlatform.getKoin().get<ViewMealDetails>() } returns viewMealDetails
        suggestionMealsService = SuggestionMealsService()
        System.setOut(java.io.PrintStream(outputStream))
    }

    @AfterEach
    fun clear() {
        outputStream.reset()
        unmockkAll()
    }

// region keto meal service
    @Test
    fun `should launch keto meal helper when user selects option 1`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        mockUserInput("1", "exit")

        // When
        suggestionMealsService.handleUserChoice()

        // Then
        val output = outputStream.toString()
        assertThat(output).contains("🥑 Keto Meal: Keto Burger")
    }


    @Test
    fun `should display meal details when user accepts keto suggestion`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { viewMealDetails.displayMealDetails(ketoMeal) } just Runs
        mockUserInput("yes", "exit")

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        val output = outputStream.toString()
        assertThat(output).contains("🥑 Keto Meal: Keto Burger")
        assertThat(output).contains("Short Description: A keto-friendly meal")
        verify(exactly = 1) { viewMealDetails.displayMealDetails(ketoMeal) }
    }

    @Test
    fun `should suggest new meal when user rejects keto suggestion`() {
        // Given
        val meal1 = createKetoMeal(1, "Keto Burger")
        val meal2 = createKetoMeal(2, "Keto Salad")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returnsMany listOf(meal1, meal2)
        mockUserInput("no", "exit")

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        val output = outputStream.toString()
        assertThat(output).contains("🥑 Keto Meal: Keto Burger")
        assertThat(output).contains("🔄 Okay! Let's try another one.")
        assertThat(output).contains("🥑 Keto Meal: Keto Salad")
    }

    @Test
    fun `should return suitable message when no available keto meals`() {
        // Given
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns null

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        val output = outputStream.toString()
        assertThat(output).contains("😔 No more keto meals to suggest.")
    }

    @Test
    fun `should exit keto suggestion loop when user types exit`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        mockUserInput("exit")

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        val output = outputStream.toString()
        assertThat(output).contains("🥑 Keto Meal: Keto Burger")
        verify(exactly = 0) { viewMealDetails.displayMealDetails(any()) }
    }

    @Test
    fun `should return specific message when user enter invalid input`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        mockUserInput("invalid", "exit")

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        val output = outputStream.toString()
        assertThat(output).contains("⚠️ Please type 'yes' or 'no'")
    }

    @Test
    fun `should handle null input for keto meal suggestion`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        mockUserInputWithNull()

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        val output = outputStream.toString()
        assertThat(output).contains("🥑 Keto Meal: Keto Burger")
        assertThat(output).contains("⚠️ Please type 'yes' or 'no'")
        verify(exactly = 0) { viewMealDetails.displayMealDetails(any()) }
    }

    @Test
    fun `should handle empty and blank inputs for keto meal suggestion`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        mockUserInput("empty", "blank", "exit")

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        val output = outputStream.toString()
        assertThat(output).contains("🥑 Keto Meal: Keto Burger")
        assertThat(output).contains("⚠️ Please type 'yes' or 'no'")
        verify(exactly = 0) { viewMealDetails.displayMealDetails(any()) }
    }

    @Test
    fun `should handle all types of invalid inputs for keto meal suggestion`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        mockUserInput("null", "empty", "blank", "invalid", "exit")

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        val output = outputStream.toString()
        assertThat(output).contains("🥑 Keto Meal: Keto Burger")
        assertThat(output).contains("⚠️ Please type 'yes' or 'no'")
        verify(exactly = 0) { viewMealDetails.displayMealDetails(any()) }
    }

    private fun createKetoMeal(id: Int, name: String): Meal {
        return Meal(
            name = name,
            id = id,
            minutes = 30,
            contributorId = 1,
            submitted = LocalDate(2005, 9, 16),
            tags = listOf("keto"),
            nutrition = Nutrition(
                caloriesKcal = 500.0,
                totalFatGrams = 40.0,
                sugarGrams = 2.0,
                sodiumGrams = 1.0,
                proteinGrams = 20.0,
                saturatedFatGrams = 15.0,
                carbohydratesGrams = 10.0
            ),
            nSteps = 5,
            steps = listOf("Step 1", "Step 2"),
            description = "A keto-friendly meal",
            ingredients = listOf("Ingredient 1", "Ingredient 2"),
            nIngredients = 2
        )
    }

    private fun mockUserInputWithNull() {
        mockkStatic("kotlin.io.ConsoleKt")
        var firstCall = true
        every { readlnOrNull() } answers {
            if (firstCall) {
                firstCall = false
                null
            } else {
                "exit"
            }
        }
    }

    // Update the existing mockUserInput function to handle empty and blank inputs
    private fun mockUserInput(vararg inputs: String) {
        val inputIterator = inputs.iterator()
        mockkStatic("")
        every { readlnOrNull() } answers {
            when {
                inputIterator.hasNext() -> {
                    val next = inputIterator.next()
                    when (next) {
                        "null" -> null
                        "blank" -> "   "
                        "empty" -> ""
                        else -> next
                    }
                }

                else -> "exit"
            }
        }
    }
    // endregion

}